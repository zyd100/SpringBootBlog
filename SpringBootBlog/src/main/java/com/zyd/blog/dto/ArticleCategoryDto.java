package com.zyd.blog.dto;

import com.zyd.blog.model.ArticleCategory;
import com.zyd.blog.model.Category;

public class ArticleCategoryDto {

  private ArticleCategory articleCategory;
  
  private Category category;
  
  public ArticleCategory getArticleCategory() {
    return articleCategory;
  }
  public void setArticleCategory(ArticleCategory articleCategory) {
    this.articleCategory = articleCategory;
  }
  public Category getCategory() {
    return category;
  }
  public void setCategory(Category category) {
    this.category = category;
  }
  
}
