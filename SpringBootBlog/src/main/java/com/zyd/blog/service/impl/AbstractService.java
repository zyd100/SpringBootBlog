package com.zyd.blog.service.impl;


import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.beans.factory.annotation.Autowired;
import com.zyd.blog.Mapper;
import com.zyd.blog.exception.ServiceException;
import com.zyd.blog.service.Service;
import com.zyd.blog.util.RedisUtil;
import tk.mybatis.mapper.entity.Condition;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于通用MyBatis Mapper插件的Service接口的实现
 */
public abstract class AbstractService<T> implements Service<T> {

  @Autowired
  protected Mapper<T> mapper;

  private Class<T> modelClass; // 当前泛型真实类型的Class

  private final String REDIS_SINGLE_PREFIX_KEY;


  public static final String ALL_LIST = "list";

  public String getREDIS_SINGLE_PREFIX_KEY() {
    return REDIS_SINGLE_PREFIX_KEY;
  }

  @SuppressWarnings("unchecked")
  public AbstractService() {
    ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
    modelClass = (Class<T>) pt.getActualTypeArguments()[0];
    REDIS_SINGLE_PREFIX_KEY = modelClass.getName();
  }

  public void save(T model) {
    mapper.insertSelective(model);
    setModelToRedis(model);
    RedisDelList();
  }

  public void save(List<T> models) {
    mapper.insertList(models);
    for (T model : models) {
      setModelToRedis(model);
    }
    RedisDelList();
  }

  public void deleteById(Integer id) {
    mapper.deleteByPrimaryKey(id);
    removeModelFromRedis(id);
    RedisDelList();
  }

  public void deleteByIds(String ids) {
    mapper.deleteByIds(ids);
    for (String id : ids.split(",")) {
      if ("".equals(id) == false) {
        removeModelFromRedis(id);
      }
    }
    RedisDelList();
  }

  public void update(T model) {
    mapper.updateByPrimaryKeySelective(model);
    RedisDelList();
    setModelToRedis(findById(Integer.parseInt(getModelId(model))));
  }

  @SuppressWarnings("unchecked")
  public T findById(Integer id) {
    String key =generateModelKey(id);
    if (RedisUtil.exists(key)) {
      return (T) RedisUtil.get(key);
    } else {
      T result = mapper.selectByPrimaryKey(id);
      RedisUtil.setByDefaultTime(key, result);
      return result;
    }

  }

  @Override
  public T findBy(String fieldName, Object value) throws TooManyResultsException {
    try {
      T model = modelClass.newInstance();
      Field field = modelClass.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(model, value);
      return mapper.selectOne(model);
    } catch (ReflectiveOperationException e) {
      throw new ServiceException(e.getMessage(), e);
    }
  }

  @SuppressWarnings("unchecked")
  public List<T> findByIds(String ids) {
    List<T> list = new ArrayList<>();
    List<String> noExistId = new ArrayList<>();
    String[] idsArray = ids.split(",");
    String key;
    for (String id : idsArray) {
      key = generateModelKey(id);
      if (RedisUtil.exists(key)) {
        list.add((T) RedisUtil.get(key));
      } else {
        noExistId.add(id);
      }
    }
    if (noExistId.size() > 0) {
      List<T> searchList = mapper.selectByIds(String.join(",", noExistId));
      for (T model : searchList) {
        setModelToRedis(model);
        list.add(model);
      }
    }
    return list;
  }

  public List<T> findByCondition(Condition condition) {
    return mapper.selectByCondition(condition);
  }

  @SuppressWarnings("unchecked")
  public List<T> findAll() {
    String key = RedisUtil.generateKey(REDIS_SINGLE_PREFIX_KEY, ALL_LIST);
    if (RedisUtil.exists(key)) {
      return (List<T>) RedisUtil.get(key);
    }
    RedisUtil.setByDefaultTime(key, mapper.selectAll());
    return (List<T>) RedisUtil.get(key);
  }

  public void RedisDelAll() {

    RedisUtil.getKey(RedisUtil.generateKey(REDIS_SINGLE_PREFIX_KEY, "*")).stream()
        .forEach(x -> RedisUtil.del(x));
  }

  private String getModelId(T model) {
    try {
      Method method = modelClass.getMethod("getId");
      return Integer.toString((Integer) method.invoke(model));
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return "";
    }

  }

  private void RedisDelList() {
    String key = RedisUtil.generateKey(REDIS_SINGLE_PREFIX_KEY, ALL_LIST);
    if (RedisUtil.exists(key)) {
      RedisUtil.del(key);
    }
  }

  private void setModelToRedis(T model) {
    String key = generateModelKey(getModelId(model));
    RedisUtil.setByDefaultTime(key, model);
  }
  private void removeModelFromRedis(Integer id) {
    RedisUtil.del(generateModelKey(id));
  }
  private void removeModelFromRedis(String id) {
    RedisUtil.del(generateModelKey(id));
  }
  private String generateModelKey(Integer id) {
    return RedisUtil.generateKey(REDIS_SINGLE_PREFIX_KEY,id.toString());
  }
  private String generateModelKey(String id) {
    return RedisUtil.generateKey(REDIS_SINGLE_PREFIX_KEY,id);
  }
}
