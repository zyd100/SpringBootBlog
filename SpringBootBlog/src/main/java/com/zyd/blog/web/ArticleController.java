package com.zyd.blog.web;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.github.pagehelper.PageInfo;
import com.zyd.blog.dto.ArticleCategoryDto;
import com.zyd.blog.dto.ArticleDto;
import com.zyd.blog.dto.Result;
import com.zyd.blog.model.Article;
import com.zyd.blog.model.ArticleCategory;
import com.zyd.blog.service.ArticleCategoryService;
import com.zyd.blog.service.ArticleService;
import com.zyd.blog.service.CategoryService;
import com.zyd.blog.service.CommentService;
import com.zyd.blog.service.MinioService;
import com.zyd.blog.util.DtoGenerator;
import com.zyd.blog.util.ResultFactory;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;


@Api("ArticleController")
@RestController
@RequestMapping(path = "/articles", produces = {"application/json; charset=utf-8"})
public class ArticleController {

  @Autowired
  private ArticleService articleService;
  @Autowired
  private CommentService commentService;
  @Autowired
  private ArticleCategoryService articleCategoryService;
  @Autowired
  private MinioService minioService;
  @Autowired
  private CategoryService categoryService;

  private static final String IMG_URL_PREFIX = "http://localhost:8080/minio/view/";

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public Result<Article> saveArticle(@RequestBody Article article) {
    article.setCreatedTime(LocalDateTime.now());
    article.setSummary(StrUtil.sub(article.getContent(), 0, 100));
    articleService.save(article);
    return ResultFactory.generateSuccessResult(article);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public Result<Object> deleteArticle(@PathVariable("id") Integer id) {
    // 删除文章图片
    for (String path : StrUtil.subBetweenAll(articleService.findById(id).getContent(), "(", ")")) {
      if (path.contains("minio/view")) {
        String objectName = StrUtil.subSuf(path, IMG_URL_PREFIX.length());
        minioService.delete(objectName);
      }
    }

    // 删除文章
    articleCategoryService.deleteArticleCategoryByArticleId(id);
    commentService.deleteCommentByArticleId(id);
    articleService.deleteById(id);
    return ResultFactory.generateSuccessResult("删除成功");
  }

  @PutMapping
  @PreAuthorize("hasRole('ADMIN')")
  public Result<Article> updateArticle(@RequestBody Article article) {
    article.setUpdateTime(LocalDateTime.now());
    article.setSummary(StrUtil.sub(article.getContent(), 0, 100));
    articleService.update(article);
    return ResultFactory.generateSuccessResult(article);
  }

  @GetMapping("/{id}")
  public Result<ArticleDto> getArticle(@PathVariable int id) {

    List<ArticleCategoryDto> list = new ArrayList<ArticleCategoryDto>();

    List<ArticleCategory> articleCategories = articleCategoryService.findByArticleId(id);

    for (ArticleCategory articleCategory : articleCategories) {
      list.add(DtoGenerator.generateArticleCategoryDto(articleCategory,
          categoryService.findById(articleCategory.getCategoryId())));
    }

    return ResultFactory
        .generateSuccessResult(DtoGenerator.generateArticleDto(articleService.findById(id), list,
            commentService.findCommentsByArticleId(id, 1, 5).getList()));
  }

  @GetMapping("/summary/{pageNum}/{pageSize}")
  public Result<PageInfo<Article>> getArticleSummary(@PathVariable("pageNum") int pageNum,
      @PathVariable("pageSize") int pageSize) {
    return ResultFactory.generateSuccessResult(articleService.findAllSummary(pageNum, pageSize));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/draft/summary/{pageNum}/{pageSize}")
  public Result<PageInfo<Article>> getDraftSummary(@PathVariable("pageNum") int pageNum,
      @PathVariable("pageSize") int pageSize) {
    return ResultFactory
        .generateSuccessResult(articleService.findDraftAllSummary(pageNum, pageSize));
  }

}
