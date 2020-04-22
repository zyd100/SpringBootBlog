package com.zyd.blog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.zyd.blog.dto.Result;
import com.zyd.blog.model.Article;
import com.zyd.blog.service.ArticleService;
import com.zyd.blog.util.ResultFactory;

@Controller
@RequestMapping("/article")
public class ArticleController {

  @Autowired
  private ArticleService articleService;
  
  @GetMapping
  public String articleView() {
    return "article";
  }
  @PostMapping(produces = {"application/json; charset=utf-8"})
  @ResponseBody
  public Result<Article> saveArticle(@RequestBody  Article article){
    //articleService.save(article);
    return ResultFactory.generateSuccessResult(article);
  }
}
