package com.zyd.blog.model;

import java.time.LocalDate;
import javax.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collation = "statistic")
public class Statistic {
  @Id
  private String id;
  private String starCount;
  private String watchCount;
  private String articleCount;
  private String commentCount;
  private LocalDate date;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getStarCount() {
    return starCount;
  }

  public void setStarCount(String starCount) {
    this.starCount = starCount;
  }

  public String getWatchCount() {
    return watchCount;
  }

  public void setWatchCount(String watchCount) {
    this.watchCount = watchCount;
  }

  public String getArticleCount() {
    return articleCount;
  }

  public void setArticleCount(String articleCount) {
    this.articleCount = articleCount;
  }

  public String getCommentCount() {
    return commentCount;
  }

  public void setCommentCount(String commentCount) {
    this.commentCount = commentCount;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  @Override
  public String toString() {
    return "Statistic [id=" + id + ", starCount=" + starCount + ", watchCount=" + watchCount
        + ", articleCount=" + articleCount + ", commentCount=" + commentCount + ", date=" + date
        + "]";
  }

}
