package com.github.tbquyen.quiz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.tbquyen.datatables.DataTablesResponse;
import com.github.tbquyen.entity.Quiz;

@Service
public class QuizService {
  @Autowired
  private QuizDAO dao;

  public Quiz getEntityById(long id) {
    if (id <= 0) {
      return null;
    }

    return dao.getById(id);
  }

  public DataTablesResponse loadPage(QuizDataTablesRequest form) {
    long recordsTotal = dao.getTotalRecord(form);
    List<Quiz> categories = dao.getCurrentPage(form);

    DataTablesResponse response = new DataTablesResponse();
    response.setDraw(form.getDraw());
    response.setRecordsTotal(recordsTotal);
    response.setRecordsFiltered(recordsTotal);
    response.setData(categories);

    return response;
  }

  @Transactional(rollbackFor = Throwable.class)
  public int insert(Quiz form) {
    return dao.insert(form);
  }

  @Transactional(rollbackFor = Throwable.class)
  public int update(Quiz form) {
    return dao.update(form);
  }

  @Transactional(rollbackFor = Throwable.class)
  public int delete(long id) {
    return dao.delete(id);
  }
}
