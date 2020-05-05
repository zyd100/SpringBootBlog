package com.zyd.blog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.zyd.blog.dto.Result;
import com.zyd.blog.service.MinioService;
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
}
