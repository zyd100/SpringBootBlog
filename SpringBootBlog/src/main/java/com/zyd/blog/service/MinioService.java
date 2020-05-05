package com.zyd.blog.service;

import org.springframework.web.multipart.MultipartFile;
import com.zyd.blog.dto.MinioDto;

public interface MinioService {

  MinioDto upload(MultipartFile file);
  void delete(String fileName);
}
