package com.zyd.blog.service.impl;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zyd.blog.dto.ArticleDto;
import com.zyd.blog.mapper.ArticleMapper;
import com.zyd.blog.mapper.CommentMapper;
import com.zyd.blog.model.Article;
import com.zyd.blog.model.Comment;
import com.zyd.blog.service.ArticleService;
import tk.mybatis.mapper.entity.Example;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class ArticleServiceImpl extends AbstractService<Article> implements ArticleService {

  @Autowired
  private ArticleMapper articleMapper;
  @Autowired
  private CommentMapper commentMapper;

  private static String[] summaryArticleColum =
      {"id", "author", "title", "summary", "star", "watch"};

  @Override
  public PageInfo<Article> findAllSummary(int pageNum, int pageSize) {
    Example example = new Example(Article.class);
    example.selectProperties(summaryArticleColum);
    PageHelper.startPage(pageNum, pageSize);
    PageInfo<Article> articles = new PageInfo<Article>(articleMapper.selectByExample(example));
    return articles;
  }

  @Override
  public ArticleDto findById(int id) {
    Example example = new Example(Comment.class);
    example.createCriteria().andEqualTo("article_id", id);
    List<Comment> comments = commentMapper.selectByExample(example);
    Article article = articleMapper.selectByPrimaryKey(id);
    return generateArticleDto(article, comments);
  }

  private ArticleDto generateArticleDto(Article article, List<Comment> comments) {
    ArticleDto articleDto = new ArticleDto();
    articleDto.setArticle(article);
    articleDto.setComments(comments);
    return articleDto;
  }
}
