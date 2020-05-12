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

  private final String MODEL_NAME;


  public static final String ALL_LIST = "list";

  private String getModelId(T model) {
    try {
      Method method = modelClass.getMethod("getId");
      return Integer.toString((Integer) method.invoke(model));
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return "";
    }

  }

  
  
  public String getMODEL_NAME() {
    return MODEL_NAME;
  }

  @SuppressWarnings("unchecked")
  public AbstractService() {
    ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
    modelClass = (Class<T>) pt.getActualTypeArguments()[0];
    MODEL_NAME = modelClass.getName();
  }

  public void save(T model) {
    mapper.insertSelective(model);
    RedisUtil.setByDefaultTime(RedisUtil.generateKey(MODEL_NAME, getModelId(model)), model);
  }

  public void save(List<T> models) {
    mapper.insertList(models);
    for (T model : models) {
      RedisUtil.setByDefaultTime(RedisUtil.generateKey(MODEL_NAME, getModelId(model)), model);
    }
  }

  public void deleteById(Integer id) {
    mapper.deleteByPrimaryKey(id);
    RedisUtil.del(RedisUtil.generateKey(MODEL_NAME, Integer.toString(id)));
  }

  public void deleteByIds(String ids) {
    mapper.deleteByIds(ids);
    for (String id : ids.split(",")) {
      if ("".equals(id) == false) {
        RedisUtil.del(RedisUtil.generateKey(MODEL_NAME, id));
      }
    }
  }

  public void update(T model) {
    mapper.updateByPrimaryKeySelective(model);
    String key = RedisUtil.generateKey(MODEL_NAME, getModelId(model));
    RedisUtil.setByDefaultTime(key, model);
  }

  @SuppressWarnings("unchecked")
  public T findById(Integer id) {
    String key = RedisUtil.generateKey(MODEL_NAME, Integer.toString(id));
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
    String key;
    for (String id : ids.split(",")) {
      key = RedisUtil.generateKey(MODEL_NAME, id);
      if (RedisUtil.exists(key)) {
        list.add((T) RedisUtil.get(key));
      } else {
        noExistId.add(id);
      }
    }

    List<T> searchList = mapper.selectByIds(String.join(",", noExistId));
    for (T model : searchList) {
      key = RedisUtil.generateKey(MODEL_NAME, getModelId(model));
      RedisUtil.setByDefaultTime(key, model);
      list.add(model);
    }

    return list;
  }

  public List<T> findByCondition(Condition condition) {
    return mapper.selectByCondition(condition);
  }

  @SuppressWarnings("unchecked")
  public List<T> findAll() {
    String key = RedisUtil.generateKey(MODEL_NAME, ALL_LIST);
    if (RedisUtil.exists(key)) {
      return (List<T>) RedisUtil.get(key);
    }
    RedisUtil.set(key, mapper.selectAll(), 30);
    return (List<T>) RedisUtil.get(key);
  }
}
