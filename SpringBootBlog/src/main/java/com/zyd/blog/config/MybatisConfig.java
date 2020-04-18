package com.zyd.blog.config;


import java.util.Properties;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

@Configuration
public class MybatisConfig {
  @Bean
  public SqlSessionFactory sqlSessionFactoryBean(DataSource dataSource) throws Exception {
      SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
      factory.setDataSource(dataSource);
      factory.setTypeAliasesPackage("com.zyd.blog.model");

      //配置分页插件，详情请查阅官方文档
     /* PageHelper pageHelper = new PageHelper();
      Properties properties = new Properties();
      properties.setProperty("pageSizeZero", "true");//分页尺寸为0时查询所有纪录不再执行分页
      properties.setProperty("reasonable", "true");//页码<=0 查询第一页，页码>=总页数查询最后一页
      properties.setProperty("supportMethodsArguments", "true");//支持通过 Mapper 接口参数来传递分页参数
      pageHelper.setProperties(properties);*/
      
      //添加XML目录
      ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
      factory.setMapperLocations(resolver.getResources("classpath:mapper/*.xml"));
      return factory.getObject();
  }
  @Bean
  public MapperScannerConfigurer mapperScannerConfigurer() {
      MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
      mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactoryBean");
      mapperScannerConfigurer.setBasePackage("com.zyd.blog.mapper");

      //配置通用Mapper，详情请查阅官方文档
      Properties properties = new Properties();
      properties.setProperty("mappers", "com.zyd.blog.Mapper");
      properties.setProperty("notEmpty", "false");//insert、update是否判断字符串类型!='' 即 test="str != null"表达式内是否追加 and str != ''
      properties.setProperty("IDENTITY", "MYSQL");
      mapperScannerConfigurer.setProperties(properties);

      return mapperScannerConfigurer;
  }
}
