package com.example.server.util;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.server.detail.CustomUserDetail;
import com.example.server.model.User;
import com.example.server.service.CustomUserDetailService;

@Component
public class PrincipalUser {
  @Autowired
  private CustomUserDetailService customUserDetailService;
  
  public User getPrincipalUser() {
    CustomUserDetail customUserDetail = (CustomUserDetail)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Optional<User> optUser = customUserDetailService.findUserById(customUserDetail.getUserId());
    User user = optUser.get();
    return user;
  }

}
