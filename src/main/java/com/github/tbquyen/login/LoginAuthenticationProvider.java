package com.github.tbquyen.login;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

@Component
public class LoginAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
  private final Log LOGGER = LogFactory.getLog(getClass());
  @Autowired
  private UserDetailsService userDetailsService;
  private PasswordEncoder passwordEncoder;

  public void setUserDetailsService(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  protected void additionalAuthenticationChecks(UserDetails userDetails,
      UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
    LOGGER.debug("additionalAuthenticationChecks: " + userDetails.getUsername());

    UserDetailsImpl userDetailslmp = (UserDetailsImpl) userDetails;

    String presentedPassword = authentication.getCredentials().toString();
    if (!passwordEncoder.matches(presentedPassword, userDetailslmp.getPassword())) {
      LOGGER.debug("Password Fail");
      LoginForm form = new LoginForm();
      form.setUsername(userDetailslmp.getUsername());
      form.setPassword(presentedPassword);
      Errors errors = new BeanPropertyBindingResult(form, "LoginForm");
      errors.reject("login.001");
      throw new LoginAuthenticationException("Password fail", errors, form);
    }

    if (!userDetailslmp.isCredentialsNonExpired()) {
      LOGGER.debug("Change password");
      LoginForm form = new LoginForm();
      form.setUsername(userDetailslmp.getUsername());
      form.setPassword(presentedPassword);
      throw new PasswordExpiredException("Change password", null, form);
    }
  }

  @Override
  protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
      throws AuthenticationException {
    LOGGER.debug("retrieveUser: " + username);

    UserDetailsImpl loadedUser;

    try {
      loadedUser = (UserDetailsImpl) this.userDetailsService.loadUserByUsername(username);
    } catch (UsernameNotFoundException notFound) {
      loadedUser = null;
    } catch (Exception repositoryProblem) {
      LOGGER.debug("repository problem : " + repositoryProblem.getMessage());
      loadedUser = null;
    }

    if (loadedUser == null) {
      LOGGER.debug("Username not found: " + username);
      LoginForm form = new LoginForm();
      form.setUsername(username);
      Errors errors = new BeanPropertyBindingResult(form, "LoginForm");
      errors.reject("login.001");
      throw new LoginAuthenticationException("Username not found", errors, form);
    }

    return loadedUser;
  }

  /**
   * Creates a successful {@link MyPrincipal} object.
   *
   * @param principal      instanceof {@link UserDetailslmpl} load from database
   * @param authentication instanceof {@link MyPrincipal} with parameters when
   *                       login
   * @param user           instanceof {@link UserDetailslmpl} load from database
   *
   * @return the successful {@link MyPrincipal} token
   */
//  @Override
//  protected Authentication createSuccessAuthentication(Object principal, Authentication authentication,
//      UserDetails user) {
//    LOGGER.debug("Login Success Authentication: " + user.getUsername());
//    MyPrincipal result = MyPrincipal.getInstance(principal, authentication.getCredentials(), user.getAuthorities());
//    result.setDetails(authentication.getDetails());
//    return result;
//  }
}
