package com.zyd.blog.config;


import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.zyd.blog.enums.ResultEnum;
import com.zyd.blog.util.ResultFactory;
import cn.hutool.json.JSONUtil;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private UserDetailsService userService;
  @Autowired
  JwtAuthorizationTokenFilter jwtAuthorizationTokenFilter;

  @Bean
  public PasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  public void configure(WebSecurity web) throws Exception {

    web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userService).passwordEncoder(encoder());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    /*
     * http.authorizeRequests().antMatchers("/home").hasRole("USER") .antMatchers("/register",
     * "/article").permitAll().and().formLogin().loginPage("/login") .defaultSuccessUrl("/home");
     */

    http

        .httpBasic()
        // 未登录时，进行json格式的提示
        .authenticationEntryPoint((request, response, authException) -> {
          response.setContentType("application/json;charset=utf-8");
          response.setStatus(HttpServletResponse.SC_FORBIDDEN);
          PrintWriter out = response.getWriter();
          /*
           * Map<String,Object> map = new HashMap<String,Object>(); map.put("code",403);
           * map.put("message","未登录");
           */
          out.write(JSONUtil
              .toJsonStr(ResultFactory.generateResult(null, ResultEnum.DENIED).setMessage("未登录")));
          out.flush();
          out.close();
        })
        .and()
        .authorizeRequests()
        .antMatchers(HttpMethod.GET, // 允许对于网站静态资源的无授权访问
            "/", "/*.html",
            "/favicon.ico",
            "/**/*.html",
            "/**/*.css",
            "/**/*.js",
            "/swagger-resources/**", "/v2/api-docs/**")
        .permitAll()
        // 放行登录url
        .antMatchers("/login","/userInfos/**","/articles/**","/minio/view/**","/comments/**","/categories/**")
        .permitAll()
        .anyRequest()
        // .permitAll() //所有请求都可以访问
        .authenticated() // 其余请求必须授权才能返回
    ;
    // 取消csrf
    http.csrf().disable()

        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    http.addFilterBefore(jwtAuthorizationTokenFilter, UsernamePasswordAuthenticationFilter.class);
  }
}
