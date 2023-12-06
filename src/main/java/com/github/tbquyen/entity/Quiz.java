package com.github.tbquyen.entity;

import java.util.Date;

public class Quiz {
  private long id;
  private String name;
  private long maxQuestion;
  private long minQuestion;
  private String remark;
  private long duration;
  private Date created;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long getMaxQuestion() {
    return maxQuestion;
  }

  public void setMaxQuestion(long maxQuestion) {
    this.maxQuestion = maxQuestion;
  }

  public long getMinQuestion() {
    return minQuestion;
  }

  public void setMinQuestion(long minQuestion) {
    this.minQuestion = minQuestion;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public long getDuration() {
    return duration;
  }

  public void setDuration(long duration) {
    this.duration = duration;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }
}
