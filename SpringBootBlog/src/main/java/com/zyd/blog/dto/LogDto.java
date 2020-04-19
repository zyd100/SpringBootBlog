package com.zyd.blog.dto;


public class LogDto {

  private Integer spendTime;
  private Long startTime;
  private String description;
  private String userName;
  private String basePath;
  private String method;
  private String ip;
  private String uri;
  private String url;
  private Object parameter;
  private Object result;
  public Integer getSpendTime() {
    return spendTime;
  }
  public void setSpendTime(Integer spendTime) {
    this.spendTime = spendTime;
  }
  public Long getStartTime() {
    return startTime;
  }
  public void setStartTime(Long startTime) {
    this.startTime = startTime;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public String getUserName() {
    return userName;
  }
  public void setUserName(String userName) {
    this.userName = userName;
  }
  public String getBasePath() {
    return basePath;
  }
  public void setBasePath(String basePath) {
    this.basePath = basePath;
  }
  public String getMethod() {
    return method;
  }
  public void setMethod(String method) {
    this.method = method;
  }
  public String getIp() {
    return ip;
  }
  public void setIp(String ip) {
    this.ip = ip;
  }
  public String getUri() {
    return uri;
  }
  public void setUri(String uri) {
    this.uri = uri;
  }
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }
  public Object getParameter() {
    return parameter;
  }
  public void setParameter(Object parameter) {
    this.parameter = parameter;
  }
  public Object getResult() {
    return result;
  }
  public void setResult(Object result) {
    this.result = result;
  }
  
}
