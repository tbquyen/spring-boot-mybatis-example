package com.github.tbquyen.quiz;

import com.github.tbquyen.datatables.DataTablesRequest;

public class QuizDataTablesRequest extends DataTablesRequest {
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
