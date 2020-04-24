package com.zyd.blog.web;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zyd.blog.dto.Result;
import com.zyd.blog.model.UserInfo;
import com.zyd.blog.service.UserInfoService;
import com.zyd.blog.util.ResultFactory;

@RestController
@RequestMapping("/userInfos")
public class UserInfoController {
  
  @Autowired
  private UserInfoService userInfoService;
  
  @GetMapping
  public Result<Object>getInfo(){
    List<UserInfo>infos=userInfoService.findAll();
    return ResultFactory.generateSuccessResult(infos.size()>0?infos.get(0):null);
  }

  @PostMapping
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public Result<Object>saveInfo(@RequestBody UserInfo userInfo){
    userInfoService.save(userInfo);
    return ResultFactory.generateSuccessResult(userInfo);
  }
}
