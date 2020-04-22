package com.zyd.blog.service.impl;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zyd.blog.model.Article;
import com.zyd.blog.service.ArticleService;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class ArticleServiceImpl extends AbstractService<Article> implements ArticleService{

  
}
