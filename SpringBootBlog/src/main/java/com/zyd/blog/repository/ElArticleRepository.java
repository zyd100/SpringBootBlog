package com.zyd.blog.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import com.zyd.blog.model.Article;

public interface ElArticleRepository extends ElasticsearchRepository<Article, Integer>{
  
}
