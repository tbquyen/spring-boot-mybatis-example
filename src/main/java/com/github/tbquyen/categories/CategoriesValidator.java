package com.github.tbquyen.categories;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.github.tbquyen.entity.Categories;

@Service
public class CategoriesValidator implements Validator {

  public boolean supports(Class<?> clazz) {
    return Categories.class.isAssignableFrom(clazz);
  }

  public void validate(Object target, Errors errors) {
    ValidationUtils.rejectIfEmpty(errors, "name", "app.001", new Object[] { "tên danh mục" });
  }
}
