package com.zyd.blog.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zyd.blog.model.Category;
import com.zyd.blog.service.CategoryService;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class CategoryServiceImpl extends AbstractService<Category> implements CategoryService{

}
