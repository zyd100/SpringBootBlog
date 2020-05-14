package com.zyd.blog.service.impl;

import java.util.List;
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
public class CommentServiceImpl extends AbstractService<Comment> implements CommentService {

  @Resource
  private CommentMapper commentMapper;
  private static final String PAGE_LIST = "pageList";

  @SuppressWarnings("unchecked")
  @Override
  public PageInfo<Comment> findAll(int pageNum, int pageSize) {
    String key = RedisUtil.generateKey(getREDIS_SINGLE_PREFIX_KEY(), PAGE_LIST);
    if (RedisUtil.exists(key)) {
      return (PageInfo<Comment>) RedisUtil.get(key);
    }
    PageHelper.startPage(pageNum, pageSize);
    PageInfo<Comment> pageInfo = new PageInfo<Comment>(commentMapper.selectAll());
    RedisUtil.setByDefaultTime(key, pageInfo);
    return pageInfo;
  }

  @SuppressWarnings("unchecked")
  @Override
  public PageInfo<Comment> findCommentsByArticleId(int articleId, int pageNum, int pageSize) {
    String key = RedisUtil.generateKey(getREDIS_SINGLE_PREFIX_KEY(), PAGE_LIST,
        Integer.toString(articleId), Integer.toString(pageNum), Integer.toString(pageSize));
    if (RedisUtil.exists(key)) {
      return (PageInfo<Comment>) RedisUtil.get(key);
    }

    Example example = new Example(Comment.class);
    example.createCriteria().andEqualTo("articleId", articleId);
    PageHelper.startPage(pageNum, pageSize);
    PageInfo<Comment> pageInfo = new PageInfo<Comment>(commentMapper.selectByExample(example));
    RedisUtil.setByDefaultTime(key, pageInfo);
    return pageInfo;
  }

  @Override
  public void update(Comment model) {
    super.update(model);
    redisDelPageList();
  }

  @Override
  public void deleteById(Integer id) {
    // TODO Auto-generated method stub
    super.deleteById(id);
    redisDelPageList();
  }

  @Override
  public void deleteByIds(String ids) {
    // TODO Auto-generated method stub
    super.deleteByIds(ids);
    redisDelPageList();
  }

  @Override
  public void save(Comment model) {
    super.save(model);
    redisDelPageList();
  }
  @Override
  public void save(List<Comment> models) {
    super.save(models);
    redisDelPageList();
  }
  private void redisDelPageList() {
    String pattern = RedisUtil.generateKey(getREDIS_SINGLE_PREFIX_KEY(), PAGE_LIST+"*");
    RedisUtil.getKey(pattern).stream().forEach(x -> RedisUtil.del(x));
  }

  
  @Override
  public void deleteCommentByArticleId(Integer id) {
    Example example = new Example(Comment.class);
    example.createCriteria().andEqualTo("articleId", id);
    commentMapper.deleteByExample(example);
    redisDelPageList();
  }
}
