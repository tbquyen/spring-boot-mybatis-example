package com.github.tbquyen.categories;

import com.github.tbquyen.datatables.DataTablesRequest;

public class CategoriesDataTablesRequest extends DataTablesRequest {
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
