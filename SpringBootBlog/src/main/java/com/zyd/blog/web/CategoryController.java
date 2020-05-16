package com.zyd.blog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zyd.blog.dto.Result;
import com.zyd.blog.model.Category;
import com.zyd.blog.service.ArticleCategoryService;
import com.zyd.blog.service.CategoryService;
import com.zyd.blog.util.ResultFactory;

@RestController
@RequestMapping("/categories")
public class CategoryController {

  @Autowired
  private CategoryService categoryService;
  
  @Autowired
  private ArticleCategoryService articleCategoryService;
  
  @GetMapping
  public Result<Object>findAll(){
    return ResultFactory.generateSuccessResult(categoryService.findAll());
  }
  
  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public Result<Object>insertCategory(@RequestBody Category category){
    categoryService.save(category);
    return ResultFactory.generateSuccessResult(category);
  }
  
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public Result<Object>deleteCategory(@PathVariable("id")Integer id){
    articleCategoryService.deleteByCategoryId(id);
    categoryService.deleteById(id);
    return ResultFactory.generateSuccessResult(null);
  }
}
