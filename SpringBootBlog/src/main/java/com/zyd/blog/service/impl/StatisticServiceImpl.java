package com.zyd.blog.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zyd.blog.model.Statistic;
import com.zyd.blog.repository.StatisticRepository;
import com.zyd.blog.service.StatisticService;

@Service
public class StatisticServiceImpl implements StatisticService {

  @Autowired
  private StatisticRepository statisticRepository;

  @Override
  public void save(Statistic model) {
    model.setId(null);
    statisticRepository.save(model);
  }

  @Override
  public void save(List<Statistic> models) {
    models.forEach(x -> x.setId(null));
    statisticRepository.saveAll(models);
  }

  @Override
  public void deleteById(String id) {
    statisticRepository.findById(id).ifPresent(x -> statisticRepository.delete(x));
  }

  @Override
  public void deleteByIds(String ids) {
    // TODO Auto-generated method stub

  }

  @Override
  public void update(Statistic model) {
    statisticRepository.findById(model.getId()).ifPresent(x -> statisticRepository.save(x));
  }

  @Override
  public List<Statistic> findAll() {
    // TODO Auto-generated method stub
    return statisticRepository.findAll();
  }

  @Override
  public Statistic findById(String id) {

    return statisticRepository.findById(id).get();
  }

  @Override
  public List<Statistic> findByIds(String ids) {
    return null;
  }


}
