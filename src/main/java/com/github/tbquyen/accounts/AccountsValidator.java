package com.github.tbquyen.accounts;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.github.tbquyen.entity.Accounts;

@Service
public class AccountsValidator implements Validator {

  public boolean supports(Class<?> clazz) {
    return Accounts.class.isAssignableFrom(clazz);
  }

  public void validate(Object target, Errors errors) {
    Accounts form = (Accounts) target;

    ValidationUtils.rejectIfEmpty(errors, "username", "app.001", new Object[] { "tài khoản" });
    if (form.getId() == 0) {
      ValidationUtils.rejectIfEmpty(errors, "password", "app.001", new Object[] { "mật khẩu" });
    }
    ValidationUtils.rejectIfEmpty(errors, "role", "app.001", new Object[] { "chức vụ" });
  }
}
