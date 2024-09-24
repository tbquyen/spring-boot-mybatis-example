package com.github.tbquyen.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.github.tbquyen.config.JwtService;
import com.github.tbquyen.config.MyPrincipal;
import com.github.tbquyen.entity.Accounts;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/login")
public class LoginController {
  public static final String VIEW_NAME = "app/login";

  @Autowired
  private JwtService jwtService;
  @Autowired
  private UserDetailsServiceImpl detailsServiceImpl;
  @Autowired
  private PasswordEncoder passwordEncoder;

  @GetMapping
  public ModelAndView login(@ModelAttribute("LoginForm") Accounts loginForm,
      @RequestParam(value = "out", required = false) String out, HttpServletResponse response) {
    if (StringUtils.hasText(out)) {
      jwtService.removeToken(response);
    } else if (MyPrincipal.getInstance().isAuthenticated()) {
      return new ModelAndView("redirect:/");
    }

    return new ModelAndView(VIEW_NAME);
  }

  @PostMapping
  public ModelAndView post(ModelMap modelMap, @ModelAttribute("LoginForm") Accounts loginForm, BindingResult result,
      HttpServletResponse response) {
    if (result.hasErrors()) {
      return new ModelAndView(VIEW_NAME, HttpStatus.BAD_REQUEST);
    }

    try {
      UserDetails userDetails = detailsServiceImpl.loadUserByUsername(loginForm.getUsername());

      if (passwordEncoder.matches(loginForm.getPassword(), userDetails.getPassword())) {
        if (!userDetails.isCredentialsNonExpired()) { // mật khẩu hết hạn
          result.reject("login.002");
        } else if (!userDetails.isAccountNonLocked()) { // Tài khoản bị khóa
          result.reject("login.003");
        } else if (!userDetails.isAccountNonExpired()) { // Tài khoản hết hạn
          result.reject("login.004");
        } else if (!userDetails.isEnabled()) { // Tài khoản vô hiệu hóa
          result.reject("login.005");
        } else {
          jwtService.setTokenToCookie(response, userDetails);
          return new ModelAndView("redirect:/");
        }
      } else {
        result.reject("login.001"); // Thông tin đăng nhập không chính xác
      }
    } catch (UsernameNotFoundException e) {
      result.reject("login.001"); // Thông tin đăng nhập không chính xác
    }

    return new ModelAndView(VIEW_NAME);
  }
}
