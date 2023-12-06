package com.github.tbquyen.config;

public class AuthorizeUrl {
  private String url;
  private String roles = "";

  /**
   * @return the url
   */
  public String getUrl() {
    return url;
  }

  /**
   * @param url the url to set
   */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * @return the roles
   */
  public String getRoles() {
    return roles;
  }

  /**
   * @param roles the roles to set
   */
  public void setRoles(String roles) {
    this.roles = roles;
  }
}
