package com.zyd.blog.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zyd.blog.mapper.UserInfoMapper;
import com.zyd.blog.model.UserInfo;
import com.zyd.blog.service.UserInfoService;
import com.zyd.blog.util.RedisUtil;
import tk.mybatis.mapper.entity.Condition;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class UserInfoServiceImpl extends AbstractService<UserInfo> implements UserInfoService{

  @Resource
  private UserInfoMapper userInfoMapper;
  
  private static final String USER="user";
  
  @Override
  public UserInfo findInfoByUserId(String userId) {
    String key=RedisUtil.generateKey(getREDIS_SINGLE_PREFIX_KEY(),USER,userId.toString());
    if(RedisUtil.exists(key)) {
      return (UserInfo) RedisUtil.get(key);
    }
    Condition condition=new Condition(UserInfo.class);
    condition.createCriteria().andEqualTo("userId",userId);
    List<UserInfo>resultList=userInfoMapper.selectByCondition(condition);
    UserInfo userInfo=resultList.size()>0?resultList.get(0):null;
    RedisUtil.setByDefaultTime(key, userInfo);
    return  userInfo;
  }

  @Override
  public void update(UserInfo model) {
    super.update(model);
    redisDelByUserId(model.getUserId());
  }

  @Override
  public void deleteByIds(String ids) {
    super.deleteByIds(ids);
    redisDelAllUser();
  }
  
  @Override
  public void deleteById(Integer id) {
    super.deleteById(id);
    redisDelAllUser();
  }
  
  private void redisDelByUserId(String userId) {
    String key=RedisUtil.generateKey(getREDIS_SINGLE_PREFIX_KEY(),USER,userId);
    if(RedisUtil.exists(key)) {
      RedisUtil.del(key);
    }
  }
  
  private void redisDelAllUser() {
    String key=RedisUtil.generateKey(getREDIS_SINGLE_PREFIX_KEY(),USER,"*");
    RedisUtil.getKey(key).forEach(x->RedisUtil.del(x));
  }
}
