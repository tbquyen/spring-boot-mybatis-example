package com.github.tbquyen.config;

import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@ControllerAdvice
public class ErrorHandlerController implements ErrorController {
  private final Log LOGGER = LogFactory.getLog(getClass());
  public static final String VIEW_NAME = "app/error";

  @Autowired
  private MessageSource messageSource;

  @RequestMapping(value = "/error", method = { RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE })
  public ModelAndView renderErrorPage(final ModelMap modelMap, HttpServletRequest request) {
    Exception e = (Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    String requestedWith = request.getHeader("X-Requested-With");
    String view = VIEW_NAME;

    if (e != null) {
//      LOGGER.debug(e.getMessage());
    }

    if (status != null) {
      Integer statusCode = Integer.valueOf(status.toString());
      modelMap.addAttribute("errorCode", statusCode);
      modelMap.addAttribute("message", messageSource.getMessage("error." + statusCode, null, Locale.getDefault()));
    }

    if ("XMLHttpRequest".equals(requestedWith)) {
      view = VIEW_NAME + " :: modal-body";
    }

    LOGGER.debug(view);
    return new ModelAndView(view);
  }

  public String getErrorPath() {
    return "/error";
  }
}
