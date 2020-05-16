package com.zyd.blog.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zyd.blog.mapper.ArticleCategoryMapper;
import com.zyd.blog.model.ArticleCategory;
import com.zyd.blog.service.ArticleCategoryService;
import com.zyd.blog.util.RedisUtil;
import tk.mybatis.mapper.entity.Example;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class ArticleCetagoryServiceImpl extends AbstractService<ArticleCategory> implements ArticleCategoryService{

  @Resource
  private ArticleCategoryMapper articleCategoryMapper;
  
  private static final String REDIS_ARTICLE_PREFIX="article";
  
  @SuppressWarnings("unchecked")
  @Override
  public List<ArticleCategory> findByArticleId(Integer id) {
    String key=RedisUtil.generateKey(getREDIS_SINGLE_PREFIX_KEY(),REDIS_ARTICLE_PREFIX,id.toString());
    if(RedisUtil.exists(key)) {
      return (List<ArticleCategory>) RedisUtil.get(key);
    }
    Example example = new Example(ArticleCategory.class);
    example.createCriteria().andEqualTo("articleId", id);
    List<ArticleCategory> list=articleCategoryMapper.selectByExample(example);
    RedisUtil.setByDefaultTime(key, list);
    return list;
  }

  @Override
  public void update(ArticleCategory model) {
    super.update(model);
    redisDelByArticleId(model.getArticleId());
  }
  
  @Override
  public void deleteById(Integer id) {
    super.deleteById(id);
    redisDelByArticleId(id);
  }
  @Override
  public void deleteByIds(String ids) {
    super.deleteByIds(ids);
    redisDelAllArticle();
  }
  @Override
  public void save(ArticleCategory model) {
    super.save(model);
    redisDelByArticleId(model.getArticleId());
  }
  @Override
  public void save(List<ArticleCategory> models) {
    super.save(models);
    models.stream().forEach(x->redisDelByArticleId(x.getArticleId()));
  }
  @Override
  public void deleteArticleCategoryByArticleId(Integer id) {
    // TODO Auto-generated method stub
    Example example = new Example(ArticleCategory.class);
    example.createCriteria().andEqualTo("articleId", id);
    articleCategoryMapper.deleteByExample(example);
    redisDelByArticleId(id);
  }

  @Override
  public void deleteByCategoryId(Integer id) {
    Example example = new Example(ArticleCategory.class);
    example.createCriteria().andEqualTo("categoryId", id);
    articleCategoryMapper.deleteByExample(example);
    redisDelAllArticle();
    
  }
  
  private void redisDelByArticleId(Integer id) {
    String key=RedisUtil.generateKey(getREDIS_SINGLE_PREFIX_KEY(),REDIS_ARTICLE_PREFIX,id.toString());
    if(RedisUtil.exists(key)) {
      RedisUtil.del(key);
    }
  }
  private void redisDelAllArticle() {
    StringBuilder suffix=new StringBuilder();
    suffix.append(REDIS_ARTICLE_PREFIX);
    suffix.append("*");
    String pattern=RedisUtil.generateKey(getREDIS_SINGLE_PREFIX_KEY(),suffix.toString());
    RedisUtil.getKey(pattern).stream().forEach(x->RedisUtil.del(x));
  }

 
}
