package com.zyd.blog.model;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collation = "article")
public class Article {
  @Id
  private String id;
  private String title;
  private String content;
  private String summary;
  private String star;
  private String watch;
  private String author;
  private int type;
  private LocalDateTime updateTime;
  private LocalDateTime createdTime;
  private List<Comment> comments;
  
  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public String getWatch() {
    return watch;
  }

  public void setWatch(String watch) {
    this.watch = watch;
  }

  public LocalDateTime getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(LocalDateTime createdTime) {
    this.createdTime = createdTime;
  }

  public LocalDateTime getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(LocalDateTime updateTime) {
    this.updateTime = updateTime;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getStar() {
    return star;
  }

  public void setStar(String star) {
    this.star = star;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public List<Comment> getComments() {
    return comments;
  }

  public void setComments(List<Comment> comments) {
    this.comments = comments;
  }

  @Override
  public String toString() {
    return "Article [id=" + id + ", title=" + title + ", content=" + content + ", summary="
        + summary + ", star=" + star + ", watch=" + watch + ", author=" + author + ", type=" + type
        + ", updateTime=" + updateTime + ", createdTime=" + createdTime + ", comments=" + comments
        + "]";
  }

}
