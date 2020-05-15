package com.zyd.blog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.zyd.blog.dto.Result;
import com.zyd.blog.enums.CommentEnum;
import com.zyd.blog.model.Comment;
import com.zyd.blog.service.CommentService;
import com.zyd.blog.util.ResultFactory;

@RestController
@RequestMapping(path = "/comments", produces = {"application/json; charset=utf-8"})
public class CommentController {

  @Autowired
  private CommentService commentService;

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public Result<Object> getComments(@RequestParam("pageNum") int pageNum,
      @RequestParam("pageSize") int pageSize) {
    return ResultFactory.generateSuccessResult(commentService.findAll(pageNum, pageSize));
  }

  @GetMapping("/articles")
  public Result<Object> getCommentsByArticleId(@RequestParam("articleId") int articleId,
      @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
    return ResultFactory.generateSuccessResult(commentService.findCommentsByArticleId(articleId, pageNum, pageSize));
  }

  @PutMapping("/status")
  @PreAuthorize("hasRole('ADMIN')")
  public Result<Object> updateCommentStatus(@RequestParam("commentId") int commentId,
      @RequestParam("statusCode") int statusCode) {
    Comment comment = new Comment();
    comment.setId(commentId);
    comment.setType(CommentEnum.stateOf(statusCode).getStatusCode());
    commentService.update(comment);
    return ResultFactory.generateSuccessResult(commentService.findById(commentId));
  }

  @DeleteMapping
  @PreAuthorize("hasRole('ADMIN')")
  public Result<Object> deleteComments(@RequestParam("commentId") int commentId) {
    commentService.deleteById(commentId);
    return ResultFactory.generateSuccessResult(null);
  }
}
