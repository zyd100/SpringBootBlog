package com.zyd.blog.service;

import java.util.List;


public interface MongoService<T> {
  void save(T model);//持久化
  void save(List<T> models);//批量持久化
  void deleteById(String id);//通过主鍵刪除
  void deleteByIds(String ids);//批量刪除 eg：ids -> “1,2,3,4”
  void update(T model);//更新
  T findById(String id);//通过ID查找
  List<T> findByIds(String ids);//通过多个ID查找//eg：ids -> “1,2,3,4”
  List<T> findAll();//获取所有
}
