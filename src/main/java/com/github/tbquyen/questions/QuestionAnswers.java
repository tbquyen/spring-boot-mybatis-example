package com.github.tbquyen.questions;

import java.util.List;

import com.github.tbquyen.entity.Answers;
import com.github.tbquyen.entity.Questions;

public class QuestionAnswers extends Questions {
  private String categoryName;
  private List<Answers> answers;

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public List<Answers> getAnswers() {
    return answers;
  }

  public void setAnswers(List<Answers> answers) {
    this.answers = answers;
  }
}
