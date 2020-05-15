package com.zyd.blog.web;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zyd.blog.dto.Result;
import com.zyd.blog.model.UserInfo;
import com.zyd.blog.service.UserInfoService;
import com.zyd.blog.util.ResultFactory;
import tk.mybatis.mapper.entity.Condition;

@RestController
@RequestMapping(path="/userInfos",produces = {"application/json; charset=utf-8"})
public class UserInfoController {
  @Autowired
  private UserInfoService userInfoService;
  
  @GetMapping("/{userId}")
  public Result<Object>getInfo(@PathVariable("userId") String userId){
    Condition condition=new Condition(UserInfo.class);
    condition.createCriteria().andEqualTo("userId",userId);
    List<UserInfo> resultInfos=userInfoService.findByCondition(condition);
    return ResultFactory.generateSuccessResult(resultInfos.size()>0?resultInfos.get(0):null);
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public Result<Object>saveInfo(@RequestBody UserInfo userInfo){
    userInfoService.save(userInfo);
    return ResultFactory.generateSuccessResult(userInfo);
  }
  @PutMapping
  @PreAuthorize("hasRole('ADMIN')")
  public Result<Object>updateInfo(@RequestBody UserInfo userInfo){
    userInfoService.update(userInfo);
    return ResultFactory.generateSuccessResult(userInfo);
  }
  @DeleteMapping
  @PreAuthorize("hasRole('ADMIN')")
  public Result<Object>deleteInfo(@RequestBody UserInfo userInfo){
    userInfoService.deleteById(userInfo.getId());
    return ResultFactory.generateSuccessResult("删除成功");
  }
}
