package com.example.server.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class EncryptPassword {
  BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
  public String hashPassword(String password) {
    return bCryptPasswordEncoder.encode(password);
  }

}
