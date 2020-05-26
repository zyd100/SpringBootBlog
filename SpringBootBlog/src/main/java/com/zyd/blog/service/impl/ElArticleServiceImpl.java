package com.zyd.blog.service.impl;


import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.zyd.blog.model.Article;
import com.zyd.blog.repository.ElArticleRepository;
import com.zyd.blog.service.ElArticleService;

@Service
public class ElArticleServiceImpl implements ElArticleService {

  @Autowired
  private ElArticleRepository elArticleRepository;

  private static final String SEARCH_TITLE = "title";
  private static final String SEARCH_CONTENT = "content";


  @Override
  public Page<Article> searchArticles(String key,Pageable pageable) {

    return elArticleRepository.search(queryBuilder(key),pageable);
  }


  private QueryBuilder queryBuilder(String key) {
    return QueryBuilders.multiMatchQuery(key, SEARCH_CONTENT, SEARCH_TITLE);
  }

}
