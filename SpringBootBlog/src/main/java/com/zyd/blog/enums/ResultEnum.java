package com.zyd.blog.enums;

public enum ResultEnum {

  OK(200,"OK"),
  FAIL(400,"FAIL");
  private String statusInfo;
  private int statusCode;
  private ResultEnum(int code,String Info) {
    setStatusCode(code);
    setStatusInfo(Info);
  }
  public String getStatusInfo() {
    return statusInfo;
  }
  public void setStatusInfo(String statusInfo) {
    this.statusInfo = statusInfo;
  }
  public int getStatusCode() {
    return statusCode;
  }
  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }
  
}
