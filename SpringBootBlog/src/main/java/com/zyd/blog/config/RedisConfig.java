package com.zyd.blog.config;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;



@Configuration
public class RedisConfig {
  
  
  public ObjectMapper serializingObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }
  
  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
      RedisTemplate<String, Object> template = new RedisTemplate<>();
      template.setConnectionFactory(factory);

      // key采用String的序列化方式
      template.setKeySerializer(keySerializer());
      // hash的key也采用String的序列化方式
      template.setHashKeySerializer(keySerializer());
      // value序列化方式采用jackson
      template.setValueSerializer(valueSerializer());
      // hash的value序列化方式采用jackson
      template.setHashValueSerializer(valueSerializer());
      template.afterPropertiesSet();
      return template;
  }
  // redis键序列化使用StrngRedisSerializer
  private RedisSerializer<String> keySerializer() {
    return new StringRedisSerializer();
  }


  // redis值序列化使用json序列化器
  private Jackson2JsonRedisSerializer<Object> valueSerializer() {
    Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
    ObjectMapper om = serializingObjectMapper();
    om.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
    //om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance , 
        ObjectMapper.DefaultTyping.NON_FINAL, As.PROPERTY);
    jackson2JsonRedisSerializer.setObjectMapper(om);
    return jackson2JsonRedisSerializer;
  }


  // 缓存键自动生成器
  @Bean
  public KeyGenerator myKeyGenerator() {
    return (target, method, params) -> {
      StringBuilder sb = new StringBuilder();
      sb.append(target.getClass().getName());
      sb.append(method.getName());
      for (Object obj : params) {
        sb.append(obj.toString());
      }
      return sb.toString();
    };
  }
}
