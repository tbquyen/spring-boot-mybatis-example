package com.github.tbquyen.password;

public class PasswordForm {
  private String username;
  private String oldPassword;
  private String newPassword;
  private String reNewPassword;

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
   * @return the oldPassword
   */
  public String getOldPassword() {
    return oldPassword;
  }

  /**
   * @param oldPassword the oldPassword to set
   */
  public void setOldPassword(String oldPassword) {
    this.oldPassword = oldPassword;
  }

  /**
   * @return the newPassword
   */
  public String getNewPassword() {
    return newPassword;
  }

  /**
   * @param newPassword the newPassword to set
   */
  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }

  /**
   * @return the reNewPassword
   */
  public String getReNewPassword() {
    return reNewPassword;
  }

  /**
   * @param reNewPassword the reNewPassword to set
   */
  public void setReNewPassword(String reNewPassword) {
    this.reNewPassword = reNewPassword;
  }
}
