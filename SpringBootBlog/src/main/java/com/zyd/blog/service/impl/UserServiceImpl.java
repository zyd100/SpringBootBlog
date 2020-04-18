package com.zyd.blog.service.impl;

import org.springframework.transaction.annotation.Transactional;
import com.zyd.blog.model.User;
import com.zyd.blog.service.Service;

@org.springframework.stereotype.Service
@Transactional(rollbackFor = RuntimeException.class)
public class UserServiceImpl extends AbstractService<User> implements Service<User>{

}
