package com.zyd.blog.service;

import java.util.List;
import com.zyd.blog.model.ArticleCategory;

public interface ArticleCategoryService extends Service<ArticleCategory>{

  List<ArticleCategory> findByArticleId(Integer id);
  void deleteArticleCategoryByArticleId(Integer id);
  void deleteByCategoryId(Integer id);
  void deleteByArticleIdAndCategoryId(Integer aId,Integer cId);
}
