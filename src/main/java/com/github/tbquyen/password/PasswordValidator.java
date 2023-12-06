package com.github.tbquyen.password;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Service
public class PasswordValidator implements Validator {

  public boolean supports(Class<?> clazz) {
    return PasswordForm.class == clazz;
  }

  public void validate(Object target, Errors errors) {
    ValidationUtils.rejectIfEmpty(errors, "username", "app.001", new Object[] { "Username" });
    ValidationUtils.rejectIfEmpty(errors, "oldPassword", "app.001", new Object[] { "Password" });
    ValidationUtils.rejectIfEmpty(errors, "newPassword", "app.001", new Object[] { "Password" });
    ValidationUtils.rejectIfEmpty(errors, "reNewPassword", "app.001", new Object[] { "Password" });
  }
}
