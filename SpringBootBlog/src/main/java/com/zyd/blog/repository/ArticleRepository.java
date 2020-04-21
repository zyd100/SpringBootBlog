package com.zyd.blog.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.zyd.blog.model.Article;

@Repository
public interface ArticleRepository extends MongoRepository<Article, String>{

}
