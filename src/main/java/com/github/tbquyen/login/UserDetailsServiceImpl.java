package com.github.tbquyen.login;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  private final Log LOGGER = LogFactory.getLog(getClass());
  @Autowired
  private LoginDAO loginDAO;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    LOGGER.debug("loadUserByUsername: " + username);
    try {
      return loginDAO.loadUserByUsername(username);
    } catch (Throwable e) {
      LOGGER.error(e);
      throw new UsernameNotFoundException(e.getMessage(), e);
    }
  }
}
