package com.github.tbquyen.config;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

import com.github.tbquyen.login.UserDetailsImpl;

public class MyPrincipal extends UsernamePasswordAuthenticationToken {
  private static final Log LOGGER = LogFactory.getLog(MyPrincipal.class);
  private static final long serialVersionUID = 1L;
  private Class<? extends Authentication> authenticationClass;

  protected MyPrincipal(Class<? extends Authentication> authenticationClass, Object principal, Object credentials,
      Collection<? extends GrantedAuthority> authorities) {
    super(principal, credentials, authorities);
    this.authenticationClass = authenticationClass;
  }

  protected MyPrincipal(Class<? extends Authentication> authenticationClass, Object principal, Object credentials) {
    super(principal, credentials);
    this.authenticationClass = authenticationClass;
  }

  @Override
  public String getName() {
    if (this.getPrincipal() instanceof UserDetailsImpl userDetails) {
      return userDetails.getUsername();
    }

    return super.getName();
  }

  /**
   * GrantedAuthority objects from a comma-separated string representation (e.g.
   * "ROLE_A, ROLE_B, ROLE_C").
   *
   * @param authorityString the comma-separated string
   * @return the authorities created by tokenizing the string
   */
  public static void grantedAuthority(String authorityString) {
    Assert.hasText(authorityString, "Authorities name cannot be empty or null");

    UsernamePasswordAuthenticationToken myPrincipal = MyPrincipal.getInstance();

    if (!myPrincipal.isAuthenticated()) {
      return;
    }

    myPrincipal = new UsernamePasswordAuthenticationToken(myPrincipal.getPrincipal(), myPrincipal.getCredentials(),
        AuthorityUtils.commaSeparatedStringToAuthorityList(authorityString));

    SecurityContextHolder.getContext().setAuthentication(myPrincipal);
  }

  public static MyPrincipal getInstance() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    LOGGER.debug(authentication);

    if (authentication instanceof MyPrincipal) {
      return (MyPrincipal) authentication;
    }

    if (authentication instanceof AnonymousAuthenticationToken) {
      return new MyPrincipal(AnonymousAuthenticationToken.class, authentication.getPrincipal(),
          authentication.getCredentials());
    }

    return new MyPrincipal(authentication.getClass(), authentication.getPrincipal(), authentication.getCredentials(),
        authentication.getAuthorities());
  }

  public static MyPrincipal getInstance(Object principal, Object credentials) {
    return new MyPrincipal(UsernamePasswordAuthenticationToken.class, principal, credentials);
  }

  public boolean isAnonymous() {
    return AnonymousAuthenticationToken.class.isAssignableFrom(authenticationClass);
  }

  public boolean isRememberMe() {
    return RememberMeAuthenticationToken.class.isAssignableFrom(authenticationClass);
  }

  public boolean isFullyAuthenticated() {
    return !isAnonymous() && !isRememberMe();
  }
}
