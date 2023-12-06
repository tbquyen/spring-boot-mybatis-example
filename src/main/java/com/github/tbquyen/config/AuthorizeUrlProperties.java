package com.github.tbquyen.config;

import java.util.List;

public class AuthorizeUrlProperties {
  private List<AuthorizeUrl> authorizeurls;
  private List<AuthorizeUrl> permitall;

  /**
   * @return the authorizeurls
   */
  public List<AuthorizeUrl> getAuthorizeurls() {
    return authorizeurls;
  }

  /**
   * @param authorizeurls the authorizeurls to set
   */
  public void setAuthorizeurls(List<AuthorizeUrl> authorizeurls) {
    this.authorizeurls = authorizeurls;
  }

  /**
   * @return the permitall
   */
  public List<AuthorizeUrl> getPermitall() {
    return permitall;
  }

  /**
   * @param permitall the permitall to set
   */
  public void setPermitall(List<AuthorizeUrl> permitall) {
    this.permitall = permitall;
  }
}
