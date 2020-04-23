package com.zyd.blog.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zyd.blog.model.Comment;
import com.zyd.blog.service.CommentService;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class CommentServiceImpl extends AbstractService<Comment> implements CommentService{

}
