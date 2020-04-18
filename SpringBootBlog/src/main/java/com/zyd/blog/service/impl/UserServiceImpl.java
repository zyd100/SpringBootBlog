package com.zyd.blog.service.impl;


import org.springframework.transaction.annotation.Transactional;
import com.zyd.blog.model.User;
import com.zyd.blog.service.UserService;
import org.springframework.stereotype.Service;;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class UserServiceImpl extends AbstractService<User> implements UserService{

}
