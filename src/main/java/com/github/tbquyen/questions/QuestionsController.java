package com.github.tbquyen.questions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.github.tbquyen.datatables.DataTablesResponse;
import com.github.tbquyen.entity.Categories;

@Controller
@RequestMapping(value = "/questions")
public class QuestionsController {
  private final Log LOGGER = LogFactory.getLog(getClass());
  public static final String VIEW_HOME = "questions/questions";
  public static final String VIEW_EDIT = "questions/questionsForm :: QuestionsForm";
  @Autowired
  private QuestionsValidator validator;
  @Autowired
  private QuestionsService service;
  @Autowired
  private MessageSource messageSource;

  @InitBinder("QuestionsForm")
  public void initBinder(WebDataBinder binder) {
    binder.addValidators(validator);
  }

  @GetMapping
  public String questionsGET(ModelMap modelMap) {
    LOGGER.debug("questionsGET");
    List<Categories> categories = service.getCategories();
    modelMap.addAttribute("categories", categories);
    return VIEW_HOME;
  }

  @PostMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public String questionsGETPage(@ModelAttribute("DataTablesQuestionsRequest") QuestionsDataTablesRequest form) {
    LOGGER.debug("questionsGETPage");

    DataTablesResponse response = service.loadPage(form);
    return response.toString();
  }

  @PostMapping(value = "/file")
  @ResponseBody
  public String questionsPOSTFile(@ModelAttribute("files") MultipartFile[] files) {
    LOGGER.debug("questionsPOSTFile: " + files.length);
    long row = service.importCSV(files);
    return messageSource.getMessage("app.002", new String[] {row + " câu hỏi"}, null);
  }

  @GetMapping(value = "/{id}", consumes = {})
  public ModelAndView questionsGETID(ModelMap modelMap, @ModelAttribute("QuestionsForm") QuestionAnswers form,
      @PathVariable(name = "id") long id) {
    LOGGER.debug("questionsGETID: " + id);

    List<Categories> categories = service.getCategories();
    modelMap.addAttribute("categories", categories);

    QuestionAnswers bean = service.getQuestionsById(id);
    if (bean != null) {
      BeanUtils.copyProperties(bean, form);
    }

    return new ModelAndView(VIEW_EDIT);
  }

  @PostMapping(value = "/{id}")
  public ModelAndView questionsPOSTID(ModelMap modelMap, @ModelAttribute("QuestionsForm") @Validated QuestionAnswers form,
      BindingResult result) {
    LOGGER.debug("questionsPOSTID: " + form.getId());

    if (result.hasErrors()) {
      LOGGER.debug(result);
      ModelAndView mv = new ModelAndView(VIEW_EDIT);
      List<Categories> categories = service.getCategories();
      modelMap.addAttribute("categories", categories);
      mv.setStatus(HttpStatus.BAD_REQUEST);
      return mv;
    }

    Map<String, Object> object = new HashMap<String, Object>();

    if (form.getId() == 0) {
      int row = service.insert(form);
      if (row > 0) {
        object.put("message", messageSource.getMessage("app.002", new Object[] { "Câu hỏi" }, null));
        object.put("length", row);
      }
    } else {
      int row = service.update(form);
      if (row > 0) {
        object.put("message", messageSource.getMessage("app.003", new Object[] { "Câu hỏi" }, null));
        object.put("length", row);
      }
    }

    return new ModelAndView(new MappingJackson2JsonView(), object);
  }

  @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public String questionsDELETEID(@PathVariable(name = "id") long id) {
    LOGGER.debug("questionsDELETEID: " + id);

    int row = service.delete(id);
    JSONObject object = new JSONObject();
    if (row > 0) {
      object.put("message", messageSource.getMessage("app.004", new Object[] { "Câu hỏi" }, null));
      object.put("length", row);
    }

    return object.toString();
  }
}
