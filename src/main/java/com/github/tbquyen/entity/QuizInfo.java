package com.github.tbquyen.entity;

import java.util.Date;

public class QuizInfo {
  private long id;
  private long quizId;
  private long accountsId;
  private Date timeStart;
  private Date timeEnd;
  private String remark;
  private Date created;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getQuizId() {
    return quizId;
  }

  public void setQuizId(long quizId) {
    this.quizId = quizId;
  }

  public long getAccountsId() {
    return accountsId;
  }

  public void setAccountsId(long accountsId) {
    this.accountsId = accountsId;
  }

  public Date getTimeStart() {
    return timeStart;
  }

  public void setTimeStart(Date timeStart) {
    this.timeStart = timeStart;
  }

  public Date getTimeEnd() {
    return timeEnd;
  }

  public void setTimeEnd(Date timeEnd) {
    this.timeEnd = timeEnd;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }
}
