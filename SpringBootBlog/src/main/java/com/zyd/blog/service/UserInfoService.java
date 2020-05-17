package com.zyd.blog.service;

import com.zyd.blog.model.UserInfo;

public interface UserInfoService extends Service<UserInfo>{

  UserInfo findInfoByUserId(String userId);
}
