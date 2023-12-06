package com.github.tbquyen.login;

import org.springframework.security.authentication.AccountStatusException;
import org.springframework.validation.Errors;

public class PasswordExpiredException extends AccountStatusException {

  private static final long serialVersionUID = 1L;
  private Errors errors;
  private LoginForm loginForm;

  public PasswordExpiredException(String msg, Errors errors, LoginForm form) {
    super(msg);
    this.errors = errors;
    this.loginForm = form;
  }

  /**
   * @return the errors
   */
  public Errors getErrors() {
    return errors;
  }

  /**
   * @return the loginForm
   */
  public LoginForm getLoginForm() {
    return loginForm;
  }
}
