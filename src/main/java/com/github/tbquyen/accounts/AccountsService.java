package com.github.tbquyen.accounts;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.github.tbquyen.datatables.DataTablesResponse;
import com.github.tbquyen.entity.Accounts;

@Service
public class AccountsService {
  @Autowired
  private AccountsDAO dao;
  @Autowired
  private PasswordEncoder passwordEncoder;

  public Accounts getEntityById(long id) {
    if (id <= 0) {
      return null;
    }

    return dao.getEntityById(id);
  }

  public DataTablesResponse loadPage(AccountsDataTablesRequest form) {
    long recordsTotal = dao.getTotalRecord(form);
    List<Accounts> users = dao.getCurrentPage(form);

    DataTablesResponse response = new DataTablesResponse();
    response.setDraw(form.getDraw());
    response.setRecordsTotal(recordsTotal);
    response.setRecordsFiltered(recordsTotal);
    response.setData(users);

    return response;
  }

  @Transactional(rollbackFor = Throwable.class)
  public int insert(Accounts form) {
    String rawPassword = form.getPassword();
    form.setPassword(passwordEncoder.encode(rawPassword));
    return dao.insert(form);
  }

  @Transactional(rollbackFor = Throwable.class)
  public int update(Accounts form) {
    String rawPassword = form.getPassword();
    if (StringUtils.hasLength(rawPassword)) {
      form.setPassword(passwordEncoder.encode(rawPassword));
    }

    return dao.update(form);
  }

  @Transactional(rollbackFor = Throwable.class)
  public int delete(long id) {
    return dao.delete(id);
  }
}
