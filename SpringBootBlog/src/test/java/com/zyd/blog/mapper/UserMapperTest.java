package com.zyd.blog.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class UserMapperTest {

  @Autowired
  private UserMapper userMapper;

  @Test
  public void test() {
    System.out.println(userMapper.selectAll().toString());
  }
  
}
