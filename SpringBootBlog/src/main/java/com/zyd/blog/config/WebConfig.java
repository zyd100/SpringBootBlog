package com.zyd.blog.config;

import java.util.List;
import javax.servlet.ServletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.zyd.blog.dto.Result;
import com.zyd.blog.enums.ResultEnum;
import com.zyd.blog.exception.ServiceException;
import com.zyd.blog.util.ResultFactory;
import cn.hutool.json.JSONUtil;

@Configuration
public class WebConfig implements WebMvcConfigurer{

  private static final Logger LOGGER=LoggerFactory.getLogger(WebConfig.class);
  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    /*registry.addViewController("/login").setViewName("login");
    registry.addViewController("/home").setViewName("home");
    registry.addViewController("/articles").setViewName("article");*/
  }
  
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("GET","POST","DELETE","PUT");
  }
  
  //统一异常处理
  @Override
  public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
    resolvers.add(( request,  response,  handler,  e)->{
      response.setCharacterEncoding("UTF-8");
      response.setContentType("application/json;charset=utf-8");
      response.setStatus(200);
      Result<Object>result=new Result<>();
      if(e instanceof ServiceException) {
        result=ResultFactory.generateFailResult().setMessage(e.getMessage());
      }else if(e instanceof NoHandlerFoundException){
        result=ResultFactory.generateResult(null, ResultEnum.NOT_FOUND)
              .setMessage("接口["+request.getRequestURI()+"] 不存在");
      }else if (e instanceof ServletException) {
        result=ResultFactory.generateFailResult().setMessage(e.getMessage());
      }else {
        if(handler instanceof HandlerMethod) {
          HandlerMethod handlerMethod=(HandlerMethod) handler;
          result=ResultFactory.generateResult(null, ResultEnum.INTERNAL_SERVER_ERROR)
                .setMessage(String.format("接口 [%s] 出现异常，方法：%s.%s，异常类型：%s,异常摘要：%s",
                                  request.getRequestURI(),
                                  handlerMethod.getBean().getClass().getName(),
                                  handlerMethod.getMethod().getName(),
                                  e.getClass().toString(),
                                  e.getMessage()));
        }else {
          result=ResultFactory.generateResult(null, ResultEnum.INTERNAL_SERVER_ERROR)
                .setMessage(e.getMessage());
        }
      }
      LOGGER.error(e.getMessage(), e);
      try {
        response.getWriter().write(JSONUtil.toJsonStr(result));
      } catch (Exception e2) {
        LOGGER.error(e2.getMessage());
      }
      return new ModelAndView();
    });
  }
}
