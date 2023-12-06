package com.github.tbquyen.accounts;

import com.github.tbquyen.datatables.DataTablesRequest;

public class AccountsDataTablesRequest extends DataTablesRequest {
  private String username;
  private String role;

  /**
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * @param username the username to set
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * @return the role
   */
  public String getRole() {
    return role;
  }

  /**
   * @param role the role to set
   */
  public void setRole(String role) {
    this.role = role;
  }
}
