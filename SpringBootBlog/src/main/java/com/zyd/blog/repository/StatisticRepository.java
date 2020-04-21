package com.zyd.blog.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.zyd.blog.model.Statistic;

@Repository
public interface StatisticRepository extends MongoRepository<Statistic, String>{

}
