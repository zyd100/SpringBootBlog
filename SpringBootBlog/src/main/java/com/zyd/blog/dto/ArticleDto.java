package com.zyd.blog.dto;

import java.util.List;
import com.zyd.blog.model.Article;
import com.zyd.blog.model.Comment;

public class ArticleDto {

  private Article article;
  private List<Comment>comments;
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
  
}
