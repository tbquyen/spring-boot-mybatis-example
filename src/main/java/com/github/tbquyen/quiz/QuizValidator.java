package com.github.tbquyen.quiz;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.github.tbquyen.entity.Quiz;

@Service
public class QuizValidator implements Validator {

  public boolean supports(Class<?> clazz) {
    return Quiz.class.isAssignableFrom(clazz);
  }

  public void validate(Object target, Errors errors) {
    ValidationUtils.rejectIfEmpty(errors, "name", "app.001", new Object[] { "tên" });
    ValidationUtils.rejectIfEmpty(errors, "maxQuestion", "app.001", new Object[] { "số bài" });
    ValidationUtils.rejectIfEmpty(errors, "minQuestion", "app.001", new Object[] { "tối thiểu" });
    ValidationUtils.rejectIfEmpty(errors, "duration", "app.001", new Object[] { "thời gian" });
  }
}
