package com.zyd.blog.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.zyd.blog.model.UserInfo;

@Repository
public interface UserInfoRepository extends MongoRepository<UserInfo, String> {

}
