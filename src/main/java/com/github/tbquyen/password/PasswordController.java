package com.github.tbquyen.password;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.github.tbquyen.config.MyPrincipal;
import com.github.tbquyen.config.WebMvcConfig;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/password")
public class PasswordController {
  public static final String VIEW_NAME = "app/password";
  @Autowired
  private PasswordValidator validator;
  @Autowired
  private PasswordService service;

  @InitBinder("PasswordForm")
  public void initBinder(WebDataBinder binder) {
    binder.addValidators(validator);
  }

  @GetMapping
  public ModelAndView getMapping(HttpSession session, @ModelAttribute("PasswordForm") PasswordForm passwordForm) {
    MyPrincipal principal = MyPrincipal.getInstance();
    if (principal.isAuthenticated()) {
      passwordForm.setUsername(principal.getName());
    }

    ModelAndView mv = new ModelAndView(VIEW_NAME);
    return mv;
  }

  @PostMapping
  public ModelAndView postMapping(HttpSession session,
      @ModelAttribute("PasswordForm") @Validated PasswordForm passwordForm, BindingResult result,
      RedirectAttributes redirectAttributes) {
    ModelAndView mv = new ModelAndView(VIEW_NAME);
    if (result.hasErrors()) {
      mv.setStatus(HttpStatus.BAD_REQUEST);
      return mv;
    }

    int rcUpdate = service.updatePassword(passwordForm.getUsername(), passwordForm.getOldPassword(),
        passwordForm.getNewPassword());

    if (rcUpdate == 0) {
      result.reject("login.001");
      mv.setStatus(HttpStatus.BAD_REQUEST);
      return mv;
    }

    if(MyPrincipal.getInstance().isAuthenticated()) {
      mv.setViewName(WebMvcConfig.VIEW_HOME);
    } else {
      mv.setView(new RedirectView("/login"));
    }

    return mv;
  }
}
