package com.zyd.blog.util;

import java.util.List;
import com.zyd.blog.dto.ArticleDto;
import com.zyd.blog.dto.MinioDto;
import com.zyd.blog.enums.ArticleEnum;
import com.zyd.blog.model.Article;
import com.zyd.blog.model.ArticleCategory;
import com.zyd.blog.model.Comment;

public class DtoGenerator {

  public static ArticleDto generateArticleDto(Article article, List<ArticleCategory>articleCategories,List<Comment>comments){
    ArticleDto articleDto = new ArticleDto();
    articleDto.setArticle(article);
    articleDto.setArticleCategories(articleCategories);
    articleDto.setComments(comments);
    ArticleEnum articleEnum=ArticleEnum.stateOf(article.getType());
    articleDto.setType(articleEnum);
    return articleDto;
  }
  public static MinioDto generateMinioDto(String fileName,String pathName,String url) {
    MinioDto minioDto=new MinioDto();
    minioDto.setFileName(fileName);
    minioDto.setObjectName(pathName);
    minioDto.setUrl(url);
    return minioDto;
  }
}
