package com.zyd.blog.service.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zyd.blog.mapper.CommentMapper;
import com.zyd.blog.model.Comment;
import com.zyd.blog.service.CommentService;
import com.zyd.blog.util.RedisUtil;
import tk.mybatis.mapper.entity.Example;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class CommentServiceImpl extends AbstractService<Comment> implements CommentService{

  @Resource
  private CommentMapper commentMapper;
  private static final String PAGE_LIST="pageList";
  
  @SuppressWarnings("unchecked")
  @Override
  public PageInfo<Comment> findAll(int pageNum, int pageSize) {
    String key=RedisUtil.generateKey(getMODEL_NAME(),PAGE_LIST);
    if(RedisUtil.exists(key)) {
      return (PageInfo<Comment>) RedisUtil.get(key);
    }
    PageHelper.startPage(pageNum, pageSize);
    PageInfo<Comment>pageInfo=new PageInfo<Comment>(commentMapper.selectAll());
    RedisUtil.setByDefaultTime(key, pageInfo);
    return pageInfo;
  }

  @SuppressWarnings("unchecked")
  @Override
  public PageInfo<Comment> findCommentsByArticleId(int articleId, int pageNum, int pageSize) {
    String key=RedisUtil.generateKey(getMODEL_NAME(),PAGE_LIST,Integer.toString(articleId),Integer.toString(pageNum),Integer.toString(pageSize));
    if(RedisUtil.exists(key)) {
      return (PageInfo<Comment>) RedisUtil.get(key);
    }
    
    Example example=new Example(Comment.class);
    example.createCriteria().andEqualTo("articleId", articleId);
    PageHelper.startPage(pageNum, pageSize);
    PageInfo<Comment>pageInfo=new PageInfo<Comment>(commentMapper.selectByExample(example));
    RedisUtil.setByDefaultTime(key, pageInfo);
    return pageInfo;
  }
  
  
}
