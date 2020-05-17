package com.zyd.blog.util;

import java.util.ArrayList;
import java.util.List;
import com.zyd.blog.dto.ArticleCategoryDto;
import com.zyd.blog.dto.ArticleDto;
import com.zyd.blog.dto.MinioDto;
import com.zyd.blog.enums.ArticleEnum;
import com.zyd.blog.model.Article;
import com.zyd.blog.model.ArticleCategory;
import com.zyd.blog.model.Category;
import com.zyd.blog.model.Comment;

public class DtoGenerator {

  public static ArticleDto generateArticleDto(Article article, List<ArticleCategoryDto>articleCategories,List<Comment>comments){
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
  
  public static ArticleCategoryDto generateArticleCategoryDto(ArticleCategory articleCategory,Category category) {
    ArticleCategoryDto articleCategoryDto=new ArticleCategoryDto();
    articleCategoryDto.setArticleCategory(articleCategory);
    articleCategoryDto.setCategory(category);
    return articleCategoryDto;
  }
  
  public static List<ArticleCategoryDto>generateArticleCategoryDtoList(List<ArticleCategory>articleCategories,List<Category>categories){
    List<ArticleCategoryDto> result=new ArrayList<ArticleCategoryDto>();
    ArticleCategoryDto articleCategoryDto;
    for(int i=0,length=articleCategories.size();i<length;++i) {
      articleCategoryDto=new ArticleCategoryDto();
      articleCategoryDto.setArticleCategory(articleCategories.get(i));
      articleCategoryDto.setCategory(categories.get(i));
      result.add(articleCategoryDto);
    }
    return result;
  }
}
