package com.zyd.blog.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zyd.blog.model.UserInfo;
import com.zyd.blog.service.UserInfoService;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class UserInfoServiceImpl extends AbstractService<UserInfo> implements UserInfoService{

}
