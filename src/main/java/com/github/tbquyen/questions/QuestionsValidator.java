package com.github.tbquyen.questions;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.github.tbquyen.entity.Answers;

@Service
public class QuestionsValidator implements Validator {

  public boolean supports(Class<?> clazz) {
    return QuestionAnswers.class.isAssignableFrom(clazz);
  }

  public void validate(Object target, Errors errors) {
    QuestionAnswers form = (QuestionAnswers) target;
    List<Answers> answers = form.getAnswers();

    ValidationUtils.rejectIfEmpty(errors, "categoryId", "app.001", new Object[] { "danh mục" });
    ValidationUtils.rejectIfEmpty(errors, "content", "app.001", new Object[] { "nội dung" });

    if (form.getCategoryId() == 0) {
      errors.rejectValue("categoryId", "app.001", new Object[] { "danh mục" }, null);
    }

    if (answers == null || answers.size() == 0) {
      errors.reject("app.001", new Object[] { "đáp án" }, null);
    } else {
      int countCorrect = 0;

      for (int i = 0; i < answers.size(); i++) {
        Answers answer = answers.get(i);
        if (answer.isCorrect()) {
          countCorrect++;
        }

        if (!StringUtils.hasText(answer.getContent())) {
          errors.rejectValue("answers[" + i + "].content", "app.001", new Object[] { "đáp án" }, null);
        }
      }

      if (countCorrect == 0) {
        errors.reject("app.001", new Object[] { "đáp án" }, null);
      }
    }
  }
  
  public static void main(String[] args) {
    int[] a = new int[] {1};
    
    System.out.println(a.getClass());
  }
}
