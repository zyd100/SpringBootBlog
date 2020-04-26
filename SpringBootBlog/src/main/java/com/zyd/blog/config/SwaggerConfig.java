package com.zyd.blog.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
  @Bean
  public Docket buildDocket() {
    return new Docket(DocumentationType.SWAGGER_2).apiInfo(buildApiInf()).select()
        .apis(RequestHandlerSelectors.basePackage("com.zyd.blog.web")).paths(PathSelectors.any())
        .build().securitySchemes(securitySchemes()).securityContexts(securityContexts());
  }

  private ApiInfo buildApiInf() {
    return new ApiInfoBuilder().title("系统RESTful API文档").version("1.0").build();
  }

  private List<ApiKey> securitySchemes() {
    List<ApiKey> resultApiKeys = new ArrayList<>();
    ApiKey apiKey = new ApiKey("Authorization", "Authorization", "header");
    resultApiKeys.add(apiKey);
    return resultApiKeys;
  }

  private List<SecurityContext> securityContexts() {
    List<SecurityContext> resultList = new ArrayList<>();
    resultList.add(getContextByPath("/articles/.*"));
    return resultList;
  }

  private SecurityContext getContextByPath(String pathRegex) {
    return SecurityContext.builder().securityReferences(defaultAuth())
        .forPaths(PathSelectors.regex(pathRegex)).build();
  }

  private List<SecurityReference> defaultAuth() {
    List<SecurityReference> resultList = new ArrayList<>();
    AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
    authorizationScopes[0] = authorizationScope;
    resultList.add(new SecurityReference("Authorization", authorizationScopes));
    return resultList;
  }
}
