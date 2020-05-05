package com.zyd.blog.dto;

public class MinioDto {

  private String fileName;
  private String url;
  private String pathName;
  
  public String getPathName() {
    return pathName;
  }
  public void setPathName(String pathName) {
    this.pathName = pathName;
  }
  public String getFileName() {
    return fileName;
  }
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }

}
