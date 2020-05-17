package com.zyd.blog.web;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.zyd.blog.dto.Result;
import com.zyd.blog.model.ArticleCategory;
import com.zyd.blog.model.Category;
import com.zyd.blog.service.ArticleCategoryService;
import com.zyd.blog.service.CategoryService;
import com.zyd.blog.util.DtoGenerator;
import com.zyd.blog.util.ResultFactory;
import cn.hutool.core.util.StrUtil;

@RestController
@RequestMapping("/articleCategories")
public class ArticleCategoryController {

  @Autowired
  private ArticleCategoryService articleCategoryService;

  @Autowired
  private CategoryService categoryService;

  @GetMapping("/{articleId}")
  public Result<Object> findArticleCategories(@PathVariable("articleId") Integer articleId) {
    List<ArticleCategory> articleCategories = articleCategoryService.findByArticleId(articleId);
    if(articleCategories.size()<1) {
      return ResultFactory.generateSuccessResult(
          DtoGenerator.generateArticleCategoryDtoList(articleCategories, null));
    }
    String ids = getCategoryIds(articleCategories);
    List<Category> categories = categoryService.findByIds(ids);
    return ResultFactory.generateSuccessResult(
        DtoGenerator.generateArticleCategoryDtoList(articleCategories, categories));
  }

  @PostMapping("/{articleId}")
  @PreAuthorize("hasRole('ADMIN')")
  public Result<Object> insertCategoryToArticle(@PathVariable("articleId") Integer articleId,
      @RequestParam("categoiesIds") String categoiesIds) {
    String[] categoryList = StrUtil.split(categoiesIds, ",");
    List<ArticleCategory> articleCategories = generateArticleCategories(articleId, categoryList);
    articleCategoryService.save(articleCategories);
    return ResultFactory.generateSuccessResult(articleCategories);
  }

  @PutMapping("/{articleId}")
  @PreAuthorize("hasRole('ADMIN')")
  public Result<Object> overrideCategoryToArticle(@PathVariable("articleId") Integer articleId,
      @RequestParam("categoiesIds") String categoiesIds) {
    articleCategoryService.deleteArticleCategoryByArticleId(articleId);
    String[] categoryList = StrUtil.split(categoiesIds, ",");
    List<ArticleCategory> articleCategories = generateArticleCategories(articleId, categoryList);
    articleCategoryService.save(articleCategories);
    return ResultFactory.generateSuccessResult(articleCategories);
  }

  @DeleteMapping("/{articleId}/{categoryId}")
  @PreAuthorize("hasRole('ADMIN')")
  public Result<Object> deleteCategoryFromArticle(@PathVariable("articleId") Integer articleId,
      @PathVariable("categoryId") Integer categoryId) {
    articleCategoryService.deleteByArticleIdAndCategoryId(articleId, categoryId);
    return ResultFactory.generateSuccessResult(null);
  }

  private String getCategoryIds(List<ArticleCategory> articleCategories) {
    if(articleCategories.size()<1) {
      return "";
    }
    StringBuilder sBuilder = new StringBuilder();
    for (ArticleCategory articleCategory : articleCategories) {
      sBuilder.append(articleCategory.getCategoryId());
      sBuilder.append(",");
    }
    return sBuilder.substring(0, sBuilder.length() - 1);
  }

  private List<ArticleCategory> generateArticleCategories(Integer articleId,
      String[] categoriesId) {
    ArticleCategory articleCategory;
    List<ArticleCategory> articleCategories = new ArrayList<ArticleCategory>();
    for (String cId : categoriesId) {
      articleCategory = new ArticleCategory();
      articleCategory.setArticleId(articleId);
      articleCategory.setCategoryId(Integer.parseInt(cId));
      articleCategories.add(articleCategory);
    }
    return articleCategories;
  }
}
