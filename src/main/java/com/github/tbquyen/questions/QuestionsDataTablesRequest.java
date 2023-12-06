package com.github.tbquyen.questions;

import com.github.tbquyen.datatables.DataTablesRequest;

public class QuestionsDataTablesRequest extends DataTablesRequest {
  private long categoryId;
  private String content;

  public long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(long categoryId) {
    this.categoryId = categoryId;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}
