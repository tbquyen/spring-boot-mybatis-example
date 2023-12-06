package com.github.tbquyen.login;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.github.tbquyen.config.MyPrincipal;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
public class LoginController {
  private final Log LOGGER = LogFactory.getLog(getClass());
  public static final String VIEW_HOME = "app/login";

  @GetMapping
  public ModelAndView login(HttpSession session, HttpServletRequest request,
      @ModelAttribute("LoginForm") LoginForm loginForm, BindingResult result) {
    MyPrincipal principal = MyPrincipal.getInstance();
    if (principal.isFullyAuthenticated()) {
      LOGGER.debug("Login with acccount: " + principal.getName());
      return new ModelAndView("forward:/");
    }

    ModelAndView mv = new ModelAndView(VIEW_HOME);
    mv.setStatus(HttpStatus.UNAUTHORIZED);

    Object authenticationException = session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

    if (authenticationException instanceof SessionAuthenticationException) {
      LOGGER.debug("Login Fail: SessionAuthenticationException");
      result.reject("login.002");
    } else if (authenticationException instanceof PasswordExpiredException) {
      LOGGER.debug("Login Fail: PasswordExpiredException");
      PasswordExpiredException exception = (PasswordExpiredException) authenticationException;
      BeanUtils.copyProperties(exception.getLoginForm(), loginForm);
      return new ModelAndView(new RedirectView("/password?username=" + loginForm.getUsername()));
    } else if (authenticationException instanceof LoginAuthenticationException) {
      LOGGER.debug("Login Fail: LoginAuthenticationException");
      mv.setStatus(HttpStatus.BAD_REQUEST);
      LoginAuthenticationException exception = (LoginAuthenticationException) authenticationException;
      result.addAllErrors(exception.getErrors());
      BeanUtils.copyProperties(exception.getLoginForm(), loginForm);
    } else if (principal.isRememberMe()) {
      mv.setStatus(HttpStatus.NON_AUTHORITATIVE_INFORMATION);

      SavedRequest savedRequest = (SavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");
      if(savedRequest != null) {
        result.reject("login.003", new Object[] {savedRequest.getRedirectUrl()}, null);
      } else {
        result.reject("login.004");
      }
    }

    return mv;
  }
}
