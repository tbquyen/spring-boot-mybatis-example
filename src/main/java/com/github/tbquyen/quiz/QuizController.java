package com.github.tbquyen.quiz;

import java.util.HashMap;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.github.tbquyen.datatables.DataTablesResponse;
import com.github.tbquyen.entity.Quiz;

@Controller
@RequestMapping(value = "/quiz")
public class QuizController {
  private final Log LOGGER = LogFactory.getLog(getClass());
  public static final String VIEW_HOME = "quiz/quiz";
  public static final String VIEW_EDIT = "quiz/quizForm :: QuizForm";
  @Autowired
  private QuizValidator validator;
  @Autowired
  private QuizService service;
  @Autowired
  private MessageSource messageSource;

  @InitBinder("QuizForm")
  public void initBinder(WebDataBinder binder) {
    binder.addValidators(validator);
  }

  @GetMapping
  public String categoriesGET() {
    LOGGER.debug("categoriesGET");
    return VIEW_HOME;
  }

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public String categoriesPOST(@ModelAttribute("DataTablesUsersRequest") QuizDataTablesRequest form) {
    LOGGER.debug("categoriesPOST");

    DataTablesResponse response = service.loadPage(form);
    return response.toString();
  }

  @GetMapping(value = "/{id}")
  public ModelAndView categoriesGETID(@ModelAttribute("QuizForm") Quiz form,
      @PathVariable(name = "id") long id) {
    LOGGER.debug("categoriesGETID: " + id);

    Quiz user = service.getEntityById(id);

    if (user != null) {
      BeanUtils.copyProperties(user, form);
    }

    return new ModelAndView(VIEW_EDIT);
  }

  @PostMapping(value = "/{id}")
  public ModelAndView categoriesPOSTID(@ModelAttribute("QuizForm") @Validated Quiz form,
      BindingResult result) {
    LOGGER.debug("categoriesPOSTID: " + form.getId());

    if (result.hasErrors()) {
      ModelAndView mv = new ModelAndView(VIEW_EDIT);
      mv.setStatus(HttpStatus.BAD_REQUEST);
      return mv;
    }

    Map<String, Object> object = new HashMap<String, Object>();

    if (form.getId() == 0) {
      int row = service.insert(form);
      if (row > 0) {
        object.put("message", messageSource.getMessage("app.002", new Object[] { form.getName() }, null));
        object.put("length", row);
      }
    } else {
      int row = service.update(form);
      if (row > 0) {
        object.put("message", messageSource.getMessage("app.003", new Object[] { form.getName() }, null));
        object.put("length", row);
      }
    }

    return new ModelAndView(new MappingJackson2JsonView(), object);
  }

  @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public String categoriesDELETE(@PathVariable(name = "id") long id) {
    LOGGER.debug("categoriesPOSTID: " + id);

    int row = service.delete(id);
    JSONObject object = new JSONObject();
    if (row > 0) {
      object.put("message", messageSource.getMessage("app.004", new Object[] { id }, null));
      object.put("length", row);
    }

    return object.toString();
  }
}
