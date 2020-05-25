package com.zyd.blog.config;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchEntityMapper;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchCustomConversions;




@Configuration
public class RestClientConfig extends AbstractElasticsearchConfiguration{

  
  @Override
  public RestHighLevelClient elasticsearchClient() {
    return RestClients.create(ClientConfiguration.localhost()).rest();
  }
  
  @Bean
  @Override
  public EntityMapper entityMapper() {                     
    ElasticsearchEntityMapper entityMapper = new ElasticsearchEntityMapper(elasticsearchMappingContext(),
        new DefaultConversionService());
    entityMapper.setConversions(elasticsearchCustomConversions());
    
    return entityMapper;
  }
  
  @Bean
  @Override
  public ElasticsearchCustomConversions elasticsearchCustomConversions() {
      @SuppressWarnings("rawtypes")
      List<Converter> converters= new ArrayList<>();
      converters.add(LongToLocalDateTimeConverter.INSTANCE);
      return new ElasticsearchCustomConversions(converters);
  }

  @ReadingConverter
  static enum LongToLocalDateTimeConverter implements Converter<Long, LocalDateTime> {
      INSTANCE;

      private LongToLocalDateTimeConverter() {
      }

      public LocalDateTime convert(Long source) {
          return Instant.ofEpochMilli(source).atZone(ZoneId.systemDefault()).toLocalDateTime();
      }
  }

}
