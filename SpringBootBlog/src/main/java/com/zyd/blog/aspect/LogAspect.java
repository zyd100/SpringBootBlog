package com.zyd.blog.aspect;


import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.zyd.blog.dto.LogDto;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONUtil;

@Component
@Aspect
public class LogAspect {
  private static final Logger LOGGER=LoggerFactory.getLogger(LogAspect.class);
  @Pointcut("execution(public * com.zyd.blog.web.*.*(..))")
  public void pointCut() {}
  
  @Around("pointCut()")
  public Object doRound(ProceedingJoinPoint point) throws Throwable{
    long startTime=System.currentTimeMillis();
    //获取当前请求对象
    ServletRequestAttributes attributes=(ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
    HttpServletRequest request=attributes.getRequest();
    //记录请求信息
    LogDto logDto=new LogDto();
    Object result=point.proceed();
    Signature signature=point.getSignature();
    MethodSignature methodSignature=(MethodSignature)signature;
    Method method=methodSignature.getMethod();
    
    long endTime=System.currentTimeMillis();
    String urlStr=request.getRequestURI().toString();
    
    logDto.setBasePath(StrUtil.removeSuffix(urlStr, URLUtil.url(urlStr).getPath()));
    logDto.setIp(request.getRemoteUser());
    logDto.setMethod(request.getMethod());
    logDto.setParameter(getParameter(method, point.getArgs()));
    logDto.setResult(result);
    logDto.setSpendTime((int)(endTime-startTime));
    logDto.setStartTime(startTime);
    logDto.setUri(request.getRequestURI());
    logDto.setUrl(request.getRequestURL().toString());
    LOGGER.info("{}",JSONUtil.parse(logDto));
    return result;
  }
  
  private Object getParameter(Method method,Object[]args) {
    List<Object>argList=new ArrayList<Object>();
    Parameter[]parameters=method.getParameters();
    Map<String, Object>map;
   for(int i=0,length=parameters.length;i<length;++i) {
     /*RequestBody requestBody=parameters[i].getAnnotation(RequestBody.class);
     if(requestBody!=null) {
       argList.add(args[i]);
     }
     
     RequestParam requestParam=parameters[i].getAnnotation(RequestParam.class);
     if(requestParam!=null) {
       Map<String, Object>map=new HashMap<String, Object>();
       String key=parameters[i].getName();
       if(StrUtil.isNotEmpty(requestParam.value())) {
         key=requestParam.value();
       }
       map.put(key, args[i]);
       argList.add(map);
     }*/
     map=new HashMap<>();
     map.put(parameters[i].toString(), args[i]);
     argList.add(map);
   }
   if(argList.size()==0) {
     return null;
   }else if (argList.size()==1) {
    return argList.get(0);
  }else {
    return argList;
  }
  }
}
