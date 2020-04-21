package com.zyd.blog.model;

import java.time.LocalDateTime;

public class Comment {
private String content;
private LocalDateTime createdTime;
private String ip;
public String getContent() {
  return content;
}
public void setContent(String content) {
  this.content = content;
}
public LocalDateTime getCreatedTime() {
  return createdTime;
}
public void setCreatedTime(LocalDateTime createdTime) {
  this.createdTime = createdTime;
}
public String getIp() {
  return ip;
}
public void setIp(String ip) {
  this.ip = ip;
}
@Override
public String toString() {
  return "Comment [content=" + content + ", createdTime=" + createdTime + ", ip=" + ip + "]";
}

}
