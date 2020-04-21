package com.zyd.blog.model;

import java.util.List;
import javax.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collation = "article")
public class Article {
@Id
private String id;
private String title;
private String star;
private String author;
private int type;
private List<Comment>comments;
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
  return "Article [id=" + id + ", title=" + title + ", star=" + star + ", author=" + author
      + ", type=" + type + ", comments=" + comments + "]";
}

}
