package com.zyd.blog.dto;

public class Result<T> {
  private T data;
  private String message;
  private int code;

  public T getData() {
    return data;
  }

  public Result<T> setData(T data) {
    this.data = data;
    return this;
  }

  public String getMessage() {
    return message;
  }

  public Result<T> setMessage(String message) {
    this.message = message;
    return this;
  }

  public int getCode() {
    return code;
  }

  public Result<T> setCode(int code) {
    this.code = code;
    return this;
  }

}
