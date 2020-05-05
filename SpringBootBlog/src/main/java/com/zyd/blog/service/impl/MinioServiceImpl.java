package com.zyd.blog.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.zyd.blog.dto.MinioDto;
import com.zyd.blog.service.MinioService;
import com.zyd.blog.util.DtoGenerator;
import com.zyd.blog.util.ExceptionGenerator;
import com.zyd.blog.util.MioProps;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.IdUtil;
import io.minio.MinioClient;



@Service
public class MinioServiceImpl implements MinioService {

  @Autowired
  private MioProps minioProps;

  @Override
  public MinioDto upload(MultipartFile file) {
    MinioDto minioDto = null;
    try {
      MinioClient minioClient = new MinioClient(minioProps.getEndPoint(), minioProps.getAccessKey(),
          minioProps.getSecretKey());
      if (BooleanUtil.isTrue(minioClient.bucketExists(minioProps.getBucketName()))) {
        // 存储桶已存在,无需新建
      } else {
        // 创建存储桶
        minioClient.makeBucket(minioProps.getBucketName());
      }
      // 设置存储对象名称
      String fileName = generateFileName(file.getOriginalFilename());
      String objectName = generateObjectName(fileName);
      // 上传文件到存储桶
      minioClient.putObject(minioProps.getBucketName(), objectName, file.getInputStream(),
          file.getContentType());
      minioDto = DtoGenerator.generateMinioDto(fileName, objectName,
          minioProps.getEndPoint() + "/" + minioProps.getBucketName() + "/" + objectName);
    } catch (Exception e) {
      ExceptionGenerator.generatorServiceException("上传发生错误:" + e.getMessage());
    }
    return minioDto;
  }

  @Override
  public void delete(String fileName) {
    try {
      MinioClient minioClient = new MinioClient(minioProps.getEndPoint(), minioProps.getAccessKey(),
          minioProps.getSecretKey());
      minioClient.removeObject(minioProps.getBucketName(), fileName);
    } catch (Exception e) {
      ExceptionGenerator.generatorServiceException("文件删除失败:" + e.getMessage());
    }

  }

  private String generateFileName(String rawName) {
    return IdUtil.simpleUUID() + rawName.substring(rawName.lastIndexOf("."));
  }

  private String generateObjectName(String fileName) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
    return simpleDateFormat.format(new Date()) + "/" + fileName;
  }
}
