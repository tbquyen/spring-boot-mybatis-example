package com.github.tbquyen.categories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.tbquyen.datatables.DataTablesResponse;
import com.github.tbquyen.entity.Categories;

@Service
public class CategoriesService {
  @Autowired
  private CategoriesDAO dao;

  public Categories getEntityById(long id) {
    if (id <= 0) {
      return null;
    }

    return dao.getEntityById(id);
  }

  public DataTablesResponse loadPage(CategoriesDataTablesRequest form) {
    long recordsTotal = dao.getTotalRecord(form);
    List<Categories> categories = dao.getCurrentPage(form);

    DataTablesResponse response = new DataTablesResponse();
    response.setDraw(form.getDraw());
    response.setRecordsTotal(recordsTotal);
    response.setRecordsFiltered(recordsTotal);
    response.setData(categories);

    return response;
  }

  @Transactional(rollbackFor = Throwable.class)
  public int insert(Categories form) {
    return dao.insert(form);
  }

  @Transactional(rollbackFor = Throwable.class)
  public int update(Categories form) {
    return dao.update(form);
  }

  @Transactional(rollbackFor = Throwable.class)
  public int delete(long id) {
    return dao.delete(id);
  }
}
