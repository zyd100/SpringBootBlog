package com.zyd.blog.mapper;

import java.util.Iterator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.zyd.blog.model.Article;
import com.zyd.blog.repository.ElArticleRepository;
import com.zyd.blog.service.ArticleService;
import com.zyd.blog.service.CommentService;
import com.zyd.blog.service.UserInfoService;

@SpringBootTest
@ExtendWith(SpringExtension.class)

public class UserMapperTest {

  @Autowired
  private UserInfoService userInfoService;
  @Autowired
  private ArticleService articleService;
  @Autowired
  private CommentService commentService;
  @Autowired
  private ElasticsearchOperations elasticsearchOperations;
  @Autowired
  private ElArticleRepository elArticleRepository;
  
  @Test
  public void test() {
   /* System.out.println(articleService.findByIds("15"));
    */
  //  Article article=articleService.findById(15);
  //  article.setContent("这是cd一句话cdd");
   /* IndexQuery indexQuery = new IndexQueryBuilder()
        .withId(article.getId().toString())
        .withObject(article)
        .build();
    System.out.println(elasticsearchOperations.index(indexQuery));*/
   // elArticleRepository.save(article);
   // articleService.update(article);
   /* Article article2=elasticsearchOperations
        .queryForObject(GetQuery.getById("15"), Article.class);*/
    //elArticleRepository.save(articleService.findById(15));
    //elasticsearchOperations.deleteIndex(Article.class);
    //elArticleRepository.delete(articleService.findById(15));
   // elArticleRepository.save(articleService.findById(15));
    //System.out.println(article2);

    
    QueryBuilder queryBuilder=QueryBuilders.matchQuery("content", "cdd");
    Iterator<Article> iterable=elArticleRepository.search(queryBuilder).iterator();
    while (iterable.hasNext()) {
      System.out.println(iterable.next());
    }
   //System.out.println(elasticsearchOperations.queryForList(criteriaQuery, Article.class));
  
  }

}
