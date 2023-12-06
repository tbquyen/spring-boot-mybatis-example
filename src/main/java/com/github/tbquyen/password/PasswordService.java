package com.github.tbquyen.password;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.tbquyen.login.LoginDAO;
import com.github.tbquyen.login.UserDetailsImpl;

@Service
public class PasswordService {
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private LoginDAO loginDAO;
  @Autowired
  private PasswordDAO changePasswordDAO;

  @Transactional(rollbackFor = Exception.class)
  public int updatePassword(String username, String password, String newpassword) {
    int rc = 0;
    UserDetailsImpl user = loginDAO.loadUserByUsername(username);
    if (user != null && passwordEncoder.matches(password, user.getPassword())) {
      rc = changePasswordDAO.updatePassword(username, passwordEncoder.encode(newpassword));
    }

    return rc;
  }
}
