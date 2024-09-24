package com.github.tbquyen.login;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.github.tbquyen.entity.Accounts;

@Service
public class LoginValidator implements Validator {

  /**
   * This Validator validates *just* LoginForm instances
   */
  public boolean supports(Class<?> clazz) {
    return Accounts.class == clazz;
  }

  public void validate(Object target, Errors errors) {
    ValidationUtils.rejectIfEmpty(errors, "username", "app.001", new Object[] { "Username" });
    ValidationUtils.rejectIfEmpty(errors, "password", "app.001", new Object[] { "Password" });
  }
}
