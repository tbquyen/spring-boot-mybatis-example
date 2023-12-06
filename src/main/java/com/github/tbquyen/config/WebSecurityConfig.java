
package com.github.tbquyen.config;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.github.tbquyen.login.LoginAuthenticationFilter;
import com.github.tbquyen.login.LoginAuthenticationProvider;
import com.github.tbquyen.login.LoginValidator;
import com.github.tbquyen.login.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
  @Autowired
  private LoginValidator loginValidator;
  @Autowired
  private UserDetailsServiceImpl userDetailsServiceImpl;
  @Autowired
  private LoginAuthenticationProvider authenticationProvider;

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  AuthenticationManager authManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
    authenticationProvider.setPasswordEncoder(passwordEncoder);
    AuthenticationManagerBuilder authenticationManagerBuilder = http
        .getSharedObject(AuthenticationManagerBuilder.class);
    authenticationManagerBuilder.authenticationProvider(authenticationProvider);
    return authenticationManagerBuilder.build();
  }

  @Bean
  SecurityContextRepository contextRepository() {
    return new HttpSessionSecurityContextRepository();
  }

  @Bean
  HttpSessionEventPublisher httpSessionEventPublisher() {
    return new HttpSessionEventPublisher();
  }

  @Bean
  SessionRegistry sessionRegistry() {
    SessionRegistry sessionRegistry = new SessionRegistryImpl();

    return sessionRegistry;
  }

  @Bean
  RememberMeServices rem() {
    return new TokenBasedRememberMeServices(TokenBasedRememberMeServices.DEFAULT_PARAMETER, userDetailsServiceImpl);
  }

  @Bean
  CompositeSessionAuthenticationStrategy sessionAuthenticationStrategy(SessionRegistry sessionRegistry) {
    ConcurrentSessionControlAuthenticationStrategy concurrentSession = new ConcurrentSessionControlAuthenticationStrategy(
        sessionRegistry);
    concurrentSession.setExceptionIfMaximumExceeded(false);
    SessionFixationProtectionStrategy sessionFixation = new SessionFixationProtectionStrategy();
    RegisterSessionAuthenticationStrategy registerSession = new RegisterSessionAuthenticationStrategy(sessionRegistry);
    CompositeSessionAuthenticationStrategy sessionAuthenticationStrategy = new CompositeSessionAuthenticationStrategy(
        Arrays.asList(concurrentSession, sessionFixation, registerSession));
    return sessionAuthenticationStrategy;
  }

  @Bean
  AuthorizeUrlProperties authorizeUrlProperties() throws IOException {
    Yaml yaml = new Yaml(new Constructor(AuthorizeUrlProperties.class));
    return yaml.load(new ClassPathResource("authorizeurls.yml").getInputStream());
  }

  @Bean
  UsernamePasswordAuthenticationFilter authenticationFilter(SecurityContextRepository repository,
      AuthenticationManager authenticationManager, RememberMeServices rem,
      CompositeSessionAuthenticationStrategy sessionAuthenticationStrategy) {
    UsernamePasswordAuthenticationFilter filter = new LoginAuthenticationFilter(loginValidator);
    filter.setAuthenticationManager(authenticationManager);
    filter.setSecurityContextRepository(repository);
    filter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler("/login?error"));
    filter.setRememberMeServices(rem);
    filter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy);
    return filter;
  }

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http, SecurityContextRepository repository, RememberMeServices rem,
      AuthorizeUrlProperties urlConfig, UsernamePasswordAuthenticationFilter filter) throws Exception {

    http.securityContext(conext -> conext.securityContextRepository(repository));

    http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/error").permitAll());

    for (AuthorizeUrl url : urlConfig.getPermitall()) {
      http.authorizeHttpRequests(authorize -> authorize.requestMatchers(url.getUrl()).permitAll());
    }

    for (AuthorizeUrl url : urlConfig.getAuthorizeurls()) {
      http.authorizeHttpRequests(
          authorize -> authorize.requestMatchers(url.getUrl()).hasAnyRole(url.getRoles().split(",")));
    }

    http.formLogin(form -> form.loginPage("/login").permitAll().loginProcessingUrl("/login").defaultSuccessUrl("/"));

    http.rememberMe(me -> me.rememberMeServices(rem));

    http.logout(logout -> logout.logoutUrl("/logout").permitAll().logoutSuccessUrl("/login?logout").permitAll()
        .invalidateHttpSession(true));

    http.authorizeHttpRequests(req -> req.anyRequest().authenticated());

    http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
        .maximumSessions(1).expiredUrl("/login?sessions-expired"));

    http.addFilterAt(filter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
