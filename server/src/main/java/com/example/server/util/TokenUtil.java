package com.example.server.util;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.server.detail.CustomUserDetail;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenUtil {

  @Value("${app.jwt.secret}")
  private String SECRET;

  private final static long EXP_DURATION = 60 * 60 * 24 * 1000;

  public String generateJwtToken(CustomUserDetail userDetail) {
    return Jwts.builder()
            .setIssuedAt(new Date())
            .setIssuer("MikeCode")
            .setSubject(String.format("%s,%s,%s", userDetail.getUserId(), userDetail.getUserEmail(), userDetail.getUsername()))  
            .setExpiration(new Date(System.currentTimeMillis() + EXP_DURATION))
            .signWith(SignatureAlgorithm.HS256, SECRET)
            .compact();
  }

  public boolean validToken(String token) {
    try {
      Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return false;
    }
  }

  public String getUserInfo(String token) {
    return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody().getSubject();
  }
}
