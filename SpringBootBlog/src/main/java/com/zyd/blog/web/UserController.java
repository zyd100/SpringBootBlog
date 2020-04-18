package com.zyd.blog.web;

import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.zyd.blog.model.User;
import com.zyd.blog.service.UserService;

@Controller
@RequestMapping("/register")
public class UserController {

  @Resource
  private UserService userService;
  @Autowired
  private PasswordEncoder encoder;
  
  @GetMapping
  public String registerForm() {
    return "registration";
  }
  @PostMapping
  public String register(User user) {
    user.setUsername("abctest");
    user.setPassword(encoder.encode(user.getPassword()));
    userService.save(user);
    return"redirect:/login";
  }
}
