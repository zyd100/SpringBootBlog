package com.zyd.blog.service.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zyd.blog.mapper.CommentMapper;
import com.zyd.blog.model.Comment;
import com.zyd.blog.service.CommentService;
import tk.mybatis.mapper.entity.Example;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class CommentServiceImpl extends AbstractService<Comment> implements CommentService{

  @Resource
  private CommentMapper commentMapper;

  @Override
  public PageInfo<Comment> findAll(int pageNum, int pageSize) {
    PageHelper.startPage(pageNum, pageSize);
    return new PageInfo<Comment>(commentMapper.selectAll());
  }

  @Override
  public PageInfo<Comment> findCommentsByArticleId(int articleId, int pageNum, int pageSize) {
    Example example=new Example(Comment.class);
    example.createCriteria().andEqualTo("articleId", articleId);
    PageHelper.startPage(pageNum, pageSize);
    return new PageInfo<Comment>(commentMapper.selectByExample(example));
  }
  
  
}
