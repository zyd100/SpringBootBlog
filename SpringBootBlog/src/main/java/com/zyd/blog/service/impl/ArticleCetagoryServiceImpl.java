package com.zyd.blog.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zyd.blog.model.ArticleCategory;
import com.zyd.blog.service.ArticleCategoryService;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class ArticleCetagoryServiceImpl extends AbstractService<ArticleCategory> implements ArticleCategoryService{

}
