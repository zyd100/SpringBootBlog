package com.zyd.blog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.zyd.blog.model.Article;

public interface ElArticleService {
  Page<Article>searchArticles(String key,Pageable pageable);
}
