package com.zyd.blog.web;

import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zyd.blog.dto.Result;
import com.zyd.blog.model.User;
import com.zyd.blog.service.UserService;
import com.zyd.blog.util.ResultFactory;

@RestController
@RequestMapping("/users")
public class UserController {

  @Resource
  private UserService userService;
  @Autowired
  private PasswordEncoder encoder;
  
  
  @PostMapping
  public Result<Object> register(@RequestBody @Valid User user) {
    user.setPassword(encoder.encode(user.getPassword()));
    userService.save(user);
    user.setPassword(null);
    return ResultFactory.generateSuccessResult(user);
  }
}
