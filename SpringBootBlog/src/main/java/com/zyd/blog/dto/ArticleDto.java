package com.zyd.blog.dto;

import java.util.List;
import com.zyd.blog.enums.ArticleEnum;
import com.zyd.blog.model.Article;
import com.zyd.blog.model.ArticleCategory;
import com.zyd.blog.model.Comment;

public class ArticleDto {

  private Article article;
  private List<Comment>comments;
  private List<ArticleCategory>articleCategories;
  private String articleType;
  private int articleTypeCode;
  
  public void setType(ArticleEnum articleEnum) {
    setArticleType(articleEnum.getStatusInfo());
    setArticleTypeCode(articleEnum.getStatusCode());
  }
  public String getArticleType() {
    return articleType;
  }
  public void setArticleType(String articleType) {
    this.articleType = articleType;
  }
  public int getArticleTypeCode() {
    return articleTypeCode;
  }
  public void setArticleTypeCode(int articleTypeCode) {
    this.articleTypeCode = articleTypeCode;
  }
  public List<ArticleCategory> getArticleCategories() {
    return articleCategories;
  }
  public void setArticleCategories(List<ArticleCategory> articleCategories) {
    this.articleCategories = articleCategories;
  }
  public Article getArticle() {
    return article;
  }
  public ArticleDto setArticle(Article article) {
    this.article = article;
    return this;
  }
  public List<Comment> getComments() {
    return comments;
  }
  public ArticleDto setComments(List<Comment> comments) {
    this.comments = comments;
    return this;
  }
  @Override
  public String toString() {
    return "ArticleDto [article=" + article + ", comments=" + comments + ", articleCategories="
        + articleCategories + ", articleType=" + articleType + ", articleTypeCode="
        + articleTypeCode + "]";
  }
  
}
