package com.zyd.blog.enums;

public enum CommentEnum {
  
  AUDIT(1, "审批中"),
  RATIFY(2, "批准");

  private String statusInfo;
  private int statusCode;

  private CommentEnum(int code, String Info) {
    setStatusCode(code);
    setStatusInfo(Info);
  }
  public static CommentEnum stateOf(int index) {
    for (CommentEnum state : values()) {
      if (state.getStatusCode() == index) {
        return state;
      }
    }
    return null;
  }

  public static CommentEnum stateOf(String index) {
    for (CommentEnum state : values()) {
      if (state.getStatusInfo().equals(index)) {
        return state;
      }
    }
    return null;
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
