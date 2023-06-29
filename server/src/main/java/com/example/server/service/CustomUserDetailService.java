package com.example.server.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.server.detail.CustomUserDetail;
import com.example.server.model.User;
import com.example.server.respository.UserRepository;

@Service
public class CustomUserDetailService implements UserDetailsService {

  @Autowired UserRepository userRepository;

  public Boolean isUserExistByEmail(String email) {
    Optional<User> optUser = userRepository.findUserByEmail(email);
    if(optUser.isEmpty()) {
      return false;
    }
    return true;
  }
  public Optional<User> findUserById(Integer id) {
    return userRepository.findById(id);
  }
  public void saveUserToDb(User user) {
    userRepository.save(user);
  }
  // override default loadbyname to loadbyemail
  @Override
  public CustomUserDetail loadUserByUsername(String email) throws UsernameNotFoundException {
    Optional<User> optUser = userRepository.findUserByEmail(email);
    if(optUser.isEmpty()) {
      throw new UsernameNotFoundException("User with email: " + email + "Not Found");
    }
    return new CustomUserDetail(optUser.get());
  }
}
