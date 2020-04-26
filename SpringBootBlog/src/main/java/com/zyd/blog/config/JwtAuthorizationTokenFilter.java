package com.zyd.blog.config;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.zyd.blog.util.JwtProps;
import com.zyd.blog.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {

  @Autowired
  private UserDetailsService userDetailsService;
  @Autowired
  private JwtUtil jwtUtil;
  @Autowired
  private JwtProps jwtProps;
  
  private Logger logger=LoggerFactory.getLogger(getClass());
  
  private static final String TOKENPREFIX="Bearer ";
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    
    final String requestHeader = request.getHeader(jwtProps.getTokenHeader());
    String username = null;
    String authToken = null;
    if (requestHeader != null && requestHeader.startsWith(TOKENPREFIX)) {
     // authToken =requestHeader.split(" ")[1];
          authToken=requestHeader.substring(TOKENPREFIX.length());
      try {
        username = jwtUtil.getUsernameFromToken(authToken);
      } catch (ExpiredJwtException e) {
        logger.error("expired token exception:"+e.getMessage());
      }catch (Exception e) {
        logger.error(e.getMessage());
      }
    }

    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

      UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

      if (jwtUtil.validateToken(authToken, userDetails)) {
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }

    }
    filterChain.doFilter(request, response);

  }

}
