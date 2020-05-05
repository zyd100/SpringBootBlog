package com.zyd.blog.util;

import com.zyd.blog.exception.ServiceException;

public class ExceptionGenerator {

  public static void generatorServiceException(String message) {
    throw new ServiceException(message);
  }
}
