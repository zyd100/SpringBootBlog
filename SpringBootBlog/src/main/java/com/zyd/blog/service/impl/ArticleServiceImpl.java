package com.zyd.blog.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zyd.blog.enums.ArticleEnum;
import com.zyd.blog.mapper.ArticleMapper;
import com.zyd.blog.model.Article;
import com.zyd.blog.service.ArticleService;
import com.zyd.blog.util.RedisUtil;
import tk.mybatis.mapper.entity.Example;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class ArticleServiceImpl extends AbstractService<Article> implements ArticleService {

  @Autowired
  private ArticleMapper articleMapper;

  private static String[] summaryArticleColum =
      {"id", "author", "title", "summary", "star", "watch", "type"};

  private static final String SUMMARY = "summary";

  @Override
  public PageInfo<Article> findAllSummary(int pageNum, int pageSize) {
    return findSummary(ArticleEnum.ARTICLE.getStatusCode(), pageNum, pageSize);
  }



  @Override
  public PageInfo<Article> findDraftAllSummary(int pageNum, int pageSize) {
    return findSummary(ArticleEnum.DRAFT.getStatusCode(), pageNum, pageSize);
  }

  @Override
  public void deleteById(Integer id) {
    super.deleteById(id);
    redisDelSummary();
  }

  @Override
  public void save(Article model) {
    super.save(model);
    redisDelSummary();
  }
  @Override
  public void save(List<Article> models) {
    super.save(models);
    redisDelSummary();
  }
  @Override
  public void deleteByIds(String ids) {
    super.deleteByIds(ids);
    redisDelSummary();
  }
  @Override
  public void update(Article model) {
    super.update(model);
    redisDelSummary();
  }

  @SuppressWarnings("unchecked")
  private PageInfo<Article> findSummary(int type, int pageNum, int pageSize) {
    String key = RedisUtil.generateKey(getREDIS_SINGLE_PREFIX_KEY(), SUMMARY,
        Integer.toString(type), Integer.toString(pageNum), Integer.toString(pageSize));
    if (RedisUtil.exists(key)) {
      return (PageInfo<Article>) RedisUtil.get(key);
    }
    Example example = new Example(Article.class);
    example.createCriteria().andEqualTo("type", type);
    example.selectProperties(summaryArticleColum);
    PageHelper.startPage(pageNum, pageSize);
    PageInfo<Article> articles = new PageInfo<Article>(articleMapper.selectByExample(example));
    RedisUtil.set(key, articles, 30);
    return articles;
  }


  private void redisDelSummary() {
    StringBuilder suffix=new StringBuilder();
    suffix.append(SUMMARY);
    suffix.append("*");
    String pattern = RedisUtil.generateKey(getREDIS_SINGLE_PREFIX_KEY(), suffix.toString());
    RedisUtil.getKey(pattern).stream().forEach(x -> RedisUtil.del(x));
  }
}
