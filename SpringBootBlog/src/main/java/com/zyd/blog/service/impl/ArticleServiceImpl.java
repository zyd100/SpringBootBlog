package com.zyd.blog.service.impl;


import java.util.List;
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

  @Override
  public PageInfo<Article> findAllSummary(int pageNum, int pageSize) {
    return findSummary(ArticleEnum.ARTICLE.getStatusCode(), pageNum, pageSize);
  }

  @Override
  public ArticleDto findById(int id) {
    Example example = new Example(ArticleCategory.class);
    example.createCriteria().andEqualTo("articleId", id);
    List<ArticleCategory> articleCategories = articleCategoryMapper.selectByExample(example);
    Article article = articleMapper.selectByPrimaryKey(id);
    return DtoGenerator.generateArticleDto(article, articleCategories, null);
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
  }

  private PageInfo<Article> findSummary(int type, int pageNum, int pageSize) {
    Example example = new Example(Article.class);
    example.createCriteria().andEqualTo("type", type);
    example.selectProperties(summaryArticleColum);
    PageHelper.startPage(pageNum, pageSize);
    PageInfo<Article> articles = new PageInfo<Article>(articleMapper.selectByExample(example));
    return articles;
  }

  private void deleteCommentByArticleId(Integer id) {
    Example example=new Example(Comment.class);
    example.createCriteria().andEqualTo("articleId", id);
    commentMapper.deleteByExample(example);
  }

  private void deleteArticleCategoryByArticleId(Integer id) {
    Example example=new Example(ArticleCategory.class);
    example.createCriteria().andEqualTo("articleId", id);
   articleCategoryMapper.deleteByExample(example);
  }
}
