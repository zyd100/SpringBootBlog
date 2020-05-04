package com.zyd.blog.enums;


public enum ArticleEnum {
  DRAFT(1, "草稿"),ARTICLE(2, "文章");

  private String statusInfo;
  private int statusCode;

  private ArticleEnum(int code, String Info) {
    setStatusCode(code);
    setStatusInfo(Info);
  }
  public static ArticleEnum stateOf(int index) {
    for (ArticleEnum state : values()) {
      if (state.getStatusCode() == index) {
        return state;
      }
    }
    return null;
  }

  public static ArticleEnum stateOf(String index) {
    for (ArticleEnum state : values()) {
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
