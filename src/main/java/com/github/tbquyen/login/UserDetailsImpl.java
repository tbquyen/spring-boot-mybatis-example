package com.github.tbquyen.login;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.github.tbquyen.entity.Accounts;

public class UserDetailsImpl extends Accounts implements UserDetails, Serializable {
  private static final long serialVersionUID = 1L;
  public static final int LOCKED = -1;
  public static final int EXPIRED = 0;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return AuthorityUtils.commaSeparatedStringToAuthorityList(super.getRole());
  }

  @Override
  public String getPassword() {
    return super.getPassword();
  }

  @Override
  public String getUsername() {
    return super.getUsername();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return super.getStatus() != LOCKED;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return super.getStatus() != EXPIRED;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof UserDetailsImpl) {
      UserDetailsImpl otherUser = (UserDetailsImpl) obj;
      return super.getUsername().equals(otherUser.getUsername());
    }

    return false;
  }

  @Override
  public int hashCode() {
    return super.getUsername() != null ? super.getUsername().hashCode() : 0;
  }
}
