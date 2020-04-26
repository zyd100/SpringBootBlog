package com.zyd.blog.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")

public class JwtProps {

  private  String secret;

  private Long expiration;

  private String tokenHeader;

  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public Long getExpiration() {
    return expiration;
  }

  public void setExpiration(Long expiration) {
    this.expiration = expiration;
  }

  public String getTokenHeader() {
    return tokenHeader;
  }

  public void setTokenHeader(String tokenHeader) {
    this.tokenHeader = tokenHeader;
  }
  
  
}
