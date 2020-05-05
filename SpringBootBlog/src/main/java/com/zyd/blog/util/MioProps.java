package com.zyd.blog.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "minio")
public class MioProps {
  
  private String endPoint;
  private String bucketName;
  private String accessKey;
  private String secretKey;
  public String getEndPoint() {
    return endPoint;
  }
  public void setEndPoint(String endPoint) {
    this.endPoint = endPoint;
  }
  public String getBucketName() {
    return bucketName;
  }
  public void setBucketName(String bucketName) {
    this.bucketName = bucketName;
  }
  public String getAccessKey() {
    return accessKey;
  }
  public void setAccessKey(String accessKey) {
    this.accessKey = accessKey;
  }
  public String getSecretKey() {
    return secretKey;
  }
  public void setSecretKey(String secretKey) {
    this.secretKey = secretKey;
  }
  
}
