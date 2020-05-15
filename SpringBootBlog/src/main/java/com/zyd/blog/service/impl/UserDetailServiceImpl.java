package com.zyd.blog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.zyd.blog.mapper.UserMapper;
import com.zyd.blog.model.User;
import com.zyd.blog.util.RedisUtil;
import cn.hutool.core.util.ObjectUtil;

@Service
public class UserDetailServiceImpl implements UserDetailsService{

  @Autowired
  private UserMapper userMapper;
  
  @Override
  public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
    String key=RedisUtil.generateKey("UserDetails",id);
    if(RedisUtil.exists(key)) {
      return result((User) RedisUtil.get(key));
    }
   User user=userMapper.selectByPrimaryKey(id);
   if(ObjectUtil.isNull(user)) {
     throw new UsernameNotFoundException("User:"+id+" not found");
   }
   RedisUtil.set(key, user,60*2*60);//2h
    //return user;
   return  result(user);
  }
  
  private UserDetails result(User user) {
    return new org.springframework.security.core.userdetails.User(user.getId(), user.getPassword(), true,
        true, true,
        true, AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRole()));
  }
}
