package com.zyd.blog;

import java.util.Date;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.zyd.blog.util.JwtUtil;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class SpringBootBlogApplicationTests {

  @Autowired
  private JwtUtil jwtUtil;
	@Test
	void test() {
	 String string= jwtUtil.getUsernameFromToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0SWQiLCJleHAiOjE1ODc5MDQwMjIsImlhdCI6MTU4Nzg5NjgyMn0.GTXNKEks7xq7NwE2l6nyMqHxcM-h9cMEs4r-MQXP11dGhJRtVWbLAj3B3Mp6WMPAbJblzgJBgfYoBLF2YRVfsw");
	System.out.println(string);
	Date date= jwtUtil.getExpirationDateFromToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0SWQiLCJleHAiOjE1ODc5MDQwMjIsImlhdCI6MTU4Nzg5NjgyMn0.GTXNKEks7xq7NwE2l6nyMqHxcM-h9cMEs4r-MQXP11dGhJRtVWbLAj3B3Mp6WMPAbJblzgJBgfYoBLF2YRVfsw");
	System.out.println(date);
	}

}
