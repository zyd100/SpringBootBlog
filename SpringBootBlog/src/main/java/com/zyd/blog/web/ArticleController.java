package com.zyd.blog.web;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.github.pagehelper.PageInfo;
import com.zyd.blog.dto.ArticleDto;
import com.zyd.blog.dto.Result;
import com.zyd.blog.model.Article;
import com.zyd.blog.service.ArticleService;
import com.zyd.blog.util.ResultFactory;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;

@Api("ArticleController")
@RestController
@RequestMapping(path = "/articles", produces = {"application/json; charset=utf-8"})
public class ArticleController {

  @Autowired
  private ArticleService articleService;


  @PostMapping
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public Result<Article> saveArticle(@RequestBody Article article) {
    article.setCreatedTime(LocalDateTime.now());
    article.setSummary(StrUtil.sub(article.getContent(), 0, 100));
    articleService.save(article);
    return ResultFactory.generateSuccessResult(article);
  }

  @GetMapping("/{id}")
  public Result<ArticleDto> getArticle(@PathVariable int id) {
    return ResultFactory.generateSuccessResult(articleService.findById(id));
  }

  @GetMapping("/summary")
  public Result<PageInfo<Article>> getArticleSummary(int pageNum, int pageSize) {
    return ResultFactory.generateSuccessResult(articleService.findAllSummary(pageNum, pageSize));
  }
  
}
