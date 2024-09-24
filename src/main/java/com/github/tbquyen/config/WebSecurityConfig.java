
package com.github.tbquyen.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  AuthorizeUrlProperties authorizeUrlProperties() throws IOException {
    Yaml yaml = new Yaml(new Constructor(AuthorizeUrlProperties.class));
    return yaml.load(new ClassPathResource("authorizeurls.yml").getInputStream());
  }

  @Bean
  JwtAuthenticationFilter jwtAuthenticationFilter() {
    return new JwtAuthenticationFilter();
  }

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http, AuthorizeUrlProperties urlConfig, JwtAuthenticationFilter filter)
      throws Exception {
    http.csrf(csrf -> csrf.disable()); // tắt cơ chế bảo vệ chống lại các cuộc tấn công CSRF.
    http.cors(cors -> cors.disable()); // không kích hoạt cơ chế CORS cho ứng dụng

    for (AuthorizeUrl url : urlConfig.getPermitall()) {
      http.authorizeHttpRequests(authorize -> authorize.requestMatchers(url.getUrl()).permitAll());
    }

    for (AuthorizeUrl url : urlConfig.getAuthorizeurls()) {
      http.authorizeHttpRequests(
          authorize -> authorize.requestMatchers(url.getUrl()).hasAnyRole(url.getRoles().split(",")));
    }

    http.authorizeHttpRequests(req -> req.anyRequest().authenticated());

    http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    // kiểm tra token trước khi tiến hành các bước xác thực khác.
    http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

    http.formLogin(form -> form.disable()); // tắt xác thực bằng form đăng nhập
    http.logout(logout -> logout.disable()); // tắt chức năng đăng xuất

    return http.build();
  }
}
