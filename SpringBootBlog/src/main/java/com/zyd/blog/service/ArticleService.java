package com.zyd.blog.service;

import com.github.pagehelper.PageInfo;
import com.zyd.blog.dto.ArticleDto;
import com.zyd.blog.model.Article;

public interface ArticleService extends Service<Article>{
  public ArticleDto findById(int id);
  public PageInfo<Article> findAllSummary(int pageNum,int pageSize);
  public PageInfo<Article> findDraftAllSummary(int pageNum,int pageSize);
}
