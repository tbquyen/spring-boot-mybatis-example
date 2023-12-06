package com.github.tbquyen.login;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import com.github.tbquyen.config.MyPrincipal;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
  private final Log LOGGER = LogFactory.getLog(getClass());

  private LoginValidator loginValidator;

  public LoginAuthenticationFilter(LoginValidator loginValidator) {
    super();
    this.loginValidator = loginValidator;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException {
    LOGGER.debug("[START]attemptAuthentication");
    if (!request.getMethod().equals("POST")) {
      throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
    }

    String username = obtainUsername(request);
    String password = obtainPassword(request);

    // validate form
    LoginForm form = new LoginForm();
    form.setUsername(username);
    form.setPassword(password);

    Errors errors = new BeanPropertyBindingResult(form, "LoginForm");
    loginValidator.validate(form, errors);

    if (errors.hasErrors()) {
      LOGGER.debug("Validate LoginForm Fail");
      throw new LoginAuthenticationException("Validate Fail", errors, form);
    }

    // can setting multiple parameter
    MyPrincipal authRequest = MyPrincipal.getInstance(username, password);

    setDetails(request, authRequest);

    LOGGER.debug("[END]attemptAuthentication");
    return this.getAuthenticationManager().authenticate(authRequest);
  }
}
