package com.github.tbquyen.questions;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.github.tbquyen.categories.CategoriesDAO;
import com.github.tbquyen.categories.CategoriesDataTablesRequest;
import com.github.tbquyen.datatables.DataTablesResponse;
import com.github.tbquyen.entity.Answers;
import com.github.tbquyen.entity.Categories;

@Service
public class QuestionsService {
  @Autowired
  private QuestionsDAO dao;
  @Autowired
  private CategoriesDAO categoriesDAO;

  public QuestionAnswers getQuestionsById(long id) {
    if (id <= 0) {
      return null;
    }

    return dao.getQuestionsById(id);
  }

  public List<Answers> getAnswers(long id) {
    if (id <= 0) {
      return new ArrayList<>();
    }

    return dao.getAnswers(id);
  }

  public DataTablesResponse loadPage(QuestionsDataTablesRequest form) {
    long recordsTotal = dao.getTotalRecord(form);
    List<QuestionAnswers> questions = dao.getCurrentPage(form);

    DataTablesResponse response = new DataTablesResponse();
    response.setDraw(form.getDraw());
    response.setRecordsTotal(recordsTotal);
    response.setRecordsFiltered(recordsTotal);
    response.setData(questions);

    return response;
  }

  public List<Categories> getCategories() {
    return categoriesDAO.getCurrentPage(new CategoriesDataTablesRequest());
  }

  @Transactional(rollbackFor = Throwable.class)
  public int insert(QuestionAnswers form) {
    int count = dao.insertQuestions(form);
    long questionId = form.getId();

    for (Answers answerI : form.getAnswers()) {
      answerI.setQuestionId(questionId);
      count += dao.insertAnswers(answerI);
    }

    return count;
  }

  @Transactional(rollbackFor = Throwable.class)
  public int update(QuestionAnswers form) {
    long questionId = form.getId();
    List<Answers> answers = dao.getAnswers(questionId);
    for (Answers answerU : form.getAnswers()) {
      for (Answers answerD : answers) {
        if (answerD.getId() == answerU.getId()) {
          answers.remove(answerD);
          break;
        }
      }
    }

    int count = dao.updateQuestions(form);

    for (Answers answerD : answers) {
      count += dao.deleteAnswers(answerD.getId());
    }

    for (Answers answerU : form.getAnswers()) {
      answerU.setQuestionId(questionId);
      if (answerU.getId() == 0) {
        count += dao.insertAnswers(answerU);
      } else {
        count += dao.updateAnswers(answerU);
      }
    }

    return count;
  }

  @Transactional(rollbackFor = Throwable.class)
  public int delete(long id) {
    return dao.deleteQuestions(id);
  }

  @Transactional(rollbackFor = Throwable.class)
  public long importCSV(MultipartFile[] files) {
    return 0;
  }

  public List<QuestionAnswers> parseCsv(MultipartFile file) throws Exception {
    List<QuestionAnswers> questionAnswers = new ArrayList<QuestionAnswers>();
    return questionAnswers;
  }
}
