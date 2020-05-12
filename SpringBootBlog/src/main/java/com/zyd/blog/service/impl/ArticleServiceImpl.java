package com.zyd.blog.service.impl;


import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zyd.blog.dto.ArticleDto;
import com.zyd.blog.enums.ArticleEnum;
import com.zyd.blog.mapper.ArticleCategoryMapper;
import com.zyd.blog.mapper.ArticleMapper;
import com.zyd.blog.mapper.CommentMapper;
import com.zyd.blog.model.Article;
import com.zyd.blog.model.ArticleCategory;
import com.zyd.blog.model.Comment;
import com.zyd.blog.service.ArticleService;
import com.zyd.blog.util.DtoGenerator;
import com.zyd.blog.util.RedisUtil;
import tk.mybatis.mapper.entity.Example;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class ArticleServiceImpl extends AbstractService<Article> implements ArticleService {

  @Autowired
  private ArticleMapper articleMapper;
  @Autowired
  private CommentMapper commentMapper;
  @Autowired
  private ArticleCategoryMapper articleCategoryMapper;

  private static String[] summaryArticleColum =
      {"id", "author", "title", "summary", "star", "watch", "type"};

  private static final String SUMMARY = "summary";

  @Override
  public PageInfo<Article> findAllSummary(int pageNum, int pageSize) {
    return findSummary(ArticleEnum.ARTICLE.getStatusCode(), pageNum, pageSize);
  }

  @Override
  public ArticleDto findById(int id) {
    String key =
        RedisUtil.generateKey(getMODEL_NAME(), ArticleDto.class.toString(), Integer.toString(id));
    if (RedisUtil.exists(key)) {
      return (ArticleDto) RedisUtil.get(key);
    }
    Example example = new Example(ArticleCategory.class);
    example.createCriteria().andEqualTo("articleId", id);
    List<ArticleCategory> articleCategories = articleCategoryMapper.selectByExample(example);
    Article article = articleMapper.selectByPrimaryKey(id);
    ArticleDto articleDto = DtoGenerator.generateArticleDto(article, articleCategories, null);
    RedisUtil.setByDefaultTime(key, articleDto);
    return articleDto;
  }

  @Override
  public PageInfo<Article> findDraftAllSummary(int pageNum, int pageSize) {
    return findSummary(ArticleEnum.DRAFT.getStatusCode(), pageNum, pageSize);
  }

  @Override
  public void deleteById(Integer id) {
    deleteArticleCategoryByArticleId(id);
    deleteCommentByArticleId(id);
    articleMapper.deleteByPrimaryKey(id);
    redisDel(id);
  }

  @SuppressWarnings("unchecked")
  private PageInfo<Article> findSummary(int type, int pageNum, int pageSize) {
    String key = RedisUtil.generateKey(getMODEL_NAME(), SUMMARY, Integer.toString(type),
        Integer.toString(pageNum), Integer.toString(pageSize));
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

  private void deleteCommentByArticleId(Integer id) {
    Example example = new Example(Comment.class);
    example.createCriteria().andEqualTo("articleId", id);
    commentMapper.deleteByExample(example);
  }

  private void deleteArticleCategoryByArticleId(Integer id) {
    Example example = new Example(ArticleCategory.class);
    example.createCriteria().andEqualTo("articleId", id);
    articleCategoryMapper.deleteByExample(example);
  }

  private void redisDel(Integer id) {
    RedisUtil.del(RedisUtil.generateKey(getMODEL_NAME(), Integer.toString(id)),
        RedisUtil.generateKey(getMODEL_NAME(), ArticleDto.class.toString(), Integer.toString(id)));
    String listKey = RedisUtil.generateKey(getMODEL_NAME(), ALL_LIST);
    if (RedisUtil.exists(listKey)) {
      @SuppressWarnings("unchecked")
      List<Article> articles = (List<Article>) RedisUtil.get(listKey);
      articles =
          articles.stream().filter(x -> x.getId().equals(id) == false).collect(Collectors.toList());
      RedisUtil.set(listKey, articles,30);
    }
  }
}
