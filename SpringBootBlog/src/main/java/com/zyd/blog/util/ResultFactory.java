package com.zyd.blog.util;


import com.zyd.blog.dto.Result;
import com.zyd.blog.enums.ResultEnum;

public class ResultFactory {

  public static <T> Result<T> generateSuccessResult(T model) {
    return new Result<T>().setData(model).setCode(ResultEnum.OK.getStatusCode())
        .setMessage(ResultEnum.OK.getStatusInfo());
  }

  public static Result<Object> generateFailResult() {
    return new Result<>().setCode(ResultEnum.FAIL.getStatusCode())
        .setMessage(ResultEnum.FAIL.getStatusInfo());
  }
}
