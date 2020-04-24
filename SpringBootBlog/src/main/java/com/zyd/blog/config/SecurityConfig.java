package com.zyd.blog.config;


import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.zyd.blog.dto.Result;
import com.zyd.blog.enums.ResultEnum;
import com.zyd.blog.util.ResultFactory;
import cn.hutool.json.JSONUtil;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private UserDetailsService userService;


  @Bean
  public PasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    
    web.ignoring().antMatchers(HttpMethod.OPTIONS,"/**");
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
    //未登录时，进行json格式的提示，很喜欢这种写法，不用单独写一个又一个的类
        .authenticationEntryPoint((request,response,authException) -> {
            response.setContentType("application/json;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            PrintWriter out = response.getWriter();
            /*Map<String,Object> map = new HashMap<String,Object>();
            map.put("code",403);
            map.put("message","未登录");*/
            out.write(JSONUtil.toJsonStr(ResultFactory.generateResult(null, ResultEnum.DENIED)));
            out.flush();
            out.close();
        })
        
        .and()
        .authorizeRequests()
        .anyRequest().authenticated() //必须授权才能返回
        
        .and()
        .formLogin() //使用自带的登录
        .permitAll()
        //登录失败，返回json
        .failureHandler((request,response,ex) -> {
            response.setContentType("application/json;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter out = response.getWriter();
            Result<Object> result=new Result<>();
            result.setCode(401);
            if (ex instanceof UsernameNotFoundException || ex instanceof BadCredentialsException) {
               // map.put("message","用户名或密码错误");
                result.setMessage("用户名或密码错误");
            } else if (ex instanceof DisabledException) {
                //map.put("message","账户被禁用");
                result.setMessage("账户被禁用");
            } else {
                //map.put("message","登录失败!");
                result.setMessage("登录失败!");
            }
            out.write(JSONUtil.toJsonStr(result));
            out.flush();
            out.close();
        })
        //登录成功，返回json
        .successHandler((request,response,authentication) -> {
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write(JSONUtil.toJsonStr( ResultFactory.generateSuccessResult(authentication)));
            out.flush();
            out.close();
        })
        .and()
        .exceptionHandling()
        //没有权限，返回json
        .accessDeniedHandler((request,response,ex) -> {
            response.setContentType("application/json;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            PrintWriter out = response.getWriter();
            out.write(JSONUtil.toJsonStr(ResultFactory.generateResult(null, ResultEnum.DENIED)));
            out.flush();
            out.close();
        })
        .and()
        .logout()
        //退出成功，返回json
        .logoutSuccessHandler((request,response,authentication) -> {
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write(JSONUtil.toJsonStr( ResultFactory.generateSuccessResult(authentication)));
            out.flush();
            out.close();
        })
        .permitAll();
        //开启跨域访问
        http.cors().disable();
        //开启模拟请求，比如API POST测试工具的测试，不开启时，API POST为报403错误
        http.csrf().disable();
  }
}
