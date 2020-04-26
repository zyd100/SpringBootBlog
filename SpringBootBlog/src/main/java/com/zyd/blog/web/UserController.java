package com.zyd.blog.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.zyd.blog.dto.Result;
import com.zyd.blog.model.User;
import com.zyd.blog.util.JwtUtil;
import com.zyd.blog.util.ResultFactory;

@RestController
public class UserController {

  @Autowired
  private UserDetailsService userDetailsService;
  @Autowired
  private PasswordEncoder encoder;
  @Autowired
  private JwtUtil jwtUtil;
  /*
  @PostMapping
  public Result<Object> register(@RequestBody @Valid User user) {
    user.setPassword(encoder.encode(user.getPassword()));
    userService.save(user);
    user.setPassword(null);
    return ResultFactory.generateSuccessResult(user);
  }*/
  @PostMapping("/login")
  public Result<Object>login(@RequestBody User user){
    UserDetails userDetails=userDetailsService.loadUserByUsername(user.getId());
    if(encoder.matches(user.getPassword(), userDetails.getPassword())==false) {
      throw new BadCredentialsException("用户名或密码错误，登录失败");
    }
    final String token = jwtUtil.generateToken(userDetails);
    return ResultFactory.generateSuccessResult(token);
  }
}
