package com.zyd.blog.service;


import com.github.pagehelper.PageInfo;
import com.zyd.blog.model.Comment;

public interface CommentService extends Service<Comment>{

  PageInfo<Comment>findAll(int pageNum,int pageSize);
  PageInfo<Comment>findCommentsByArticleId(int articleId,int pageNum,int pageSize);
  void deleteCommentByArticleId(Integer id);
}
