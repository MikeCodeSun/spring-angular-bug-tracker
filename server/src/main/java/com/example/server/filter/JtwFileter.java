package com.example.server.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.server.detail.CustomUserDetail;
import com.example.server.model.User;
import com.example.server.util.TokenUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JtwFileter extends OncePerRequestFilter {
  @Autowired
  private TokenUtil tokenUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
        
    if(!isTokenExist(request)) {
      filterChain.doFilter(request, response);
      System.out.println("Token not exist");
      return;
      
    }
    String token = getToken(request);
    if(!tokenUtil.validToken(token)){
      filterChain.doFilter(request, response);
      System.out.println("Token not valid");
      return;
    }
    
    setSecurityContext(token, request);
    filterChain.doFilter(request, response);
  
  }

  private boolean isTokenExist(HttpServletRequest request) {
    String authorization = request.getHeader("Authorization");
    if (authorization == null || !authorization.startsWith("Bearer ")) {
      return false;
    }
    return true;
  }
  private String getToken(HttpServletRequest request) {
    return request.getHeader("Authorization").split(" ")[1];
  }

  private void setSecurityContext(String token, HttpServletRequest request) {
    String[] userInfo = tokenUtil.getUserInfo(token).split(",");
    User user = new User();
    user.setId(Integer.parseInt(userInfo[0]));
    user.setEmail(userInfo[1]);
    user.setName(userInfo[2]);
    CustomUserDetail customUserDetail = new CustomUserDetail(user);

    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(customUserDetail, null, null);
    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
  }
}
