package com.zyd.blog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.zyd.blog.mapper.UserMapper;
import com.zyd.blog.model.User;
import cn.hutool.core.util.ObjectUtil;

@Service
public class UserDetailServiceImpl implements UserDetailsService{

  @Autowired
  private UserMapper userMapper;
  
  @Override
  public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
   User user=userMapper.selectByPrimaryKey(id);
   if(ObjectUtil.isNull(user)) {
     throw new UsernameNotFoundException("User:"+id+" not found");
   }
  
    //return user;
   return new org.springframework.security.core.userdetails.User(user.getId(), user.getPassword(), user.isEnabled(),
       user.isAccountNonExpired(), user.isCredentialsNonExpired(),
       user.isAccountNonLocked(), AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRole()));
  }
}
