package com.zyd.blog.web;

import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;
import com.zyd.blog.dto.Result;
import com.zyd.blog.service.MinioService;
import com.zyd.blog.util.ExceptionGenerator;
import com.zyd.blog.util.ResultFactory;

@RestController
@RequestMapping(path = "/minio", produces = {"application/json; charset=utf-8"})
public class MinioController {

  @Autowired
  private MinioService minioService;

  
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping("/upload")
  public Result<Object> upload(@RequestParam("file") MultipartFile file) {
    return ResultFactory.generateSuccessResult(minioService.upload(file));
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @DeleteMapping("/delete")
  public Result<Object> delete(@RequestParam("fileName") String fileName) {
    minioService.delete(fileName);
    return ResultFactory.generateSuccessResult(null);
  }
  
  @GetMapping("/view/**")
  public void view(HttpServletRequest request, HttpServletResponse response) {
    String imgPath = extractPathFromPattern(request);

    try (InputStream inputStream = minioService.getFileInputStream(imgPath);
        OutputStream outputStream = response.getOutputStream()) {
      response.setContentType("image/jpeg;charset=utf-8");
      byte[] buf = new byte[1024];
      int len;
      while ((len = inputStream.read(buf)) > 0) {
        outputStream.write(buf, 0, len);
      }
      response.flushBuffer();
    } catch (Exception e) {
      ExceptionGenerator.generatorServiceException("预览图片失败");
    }

  }
  
  private static String extractPathFromPattern(final HttpServletRequest request) {
    String path=(String)request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
    String bestMatchPattern=(String)request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
    return new AntPathMatcher().extractPathWithinPattern(bestMatchPattern, path);
  }
}
