package com.github.tbquyen.entity;

import java.util.Date;

public class QuizQuestions {
  private long quizInfoId;
  private long questionId;
  private long answersId;
  private Date created;

  public long getQuizInfoId() {
    return quizInfoId;
  }

  public void setQuizInfoId(long quizInfoId) {
    this.quizInfoId = quizInfoId;
  }

  public long getQuestionId() {
    return questionId;
  }

  public void setQuestionId(long questionId) {
    this.questionId = questionId;
  }

  public long getAnswersId() {
    return answersId;
  }

  public void setAnswersId(long answersId) {
    this.answersId = answersId;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }
}
