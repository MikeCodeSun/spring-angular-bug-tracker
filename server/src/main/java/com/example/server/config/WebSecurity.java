package com.example.server.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.server.filter.JtwFileter;
import com.example.server.service.CustomUserDetailService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@EnableWebSecurity
public class WebSecurity {
  @Autowired
  private CustomUserDetailService customUserDetailService;
  @Autowired
  private JtwFileter jtwFileter;
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .cors(cs -> cs
        .configurationSource(corsConfigurationSource()))
      .csrf(cf -> cf
        .disable())
      .authorizeHttpRequests(ahr->ahr
        .requestMatchers("user/register", "user/login")
          .permitAll()
        .anyRequest()
          .authenticated())
      .sessionManagement(sm -> sm
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .addFilterBefore(jtwFileter, UsernamePasswordAuthenticationFilter.class)
      .exceptionHandling(eh -> eh
        .authenticationEntryPoint((HttpServletRequest request, HttpServletResponse response, AuthenticationException AuthException) -> {
          String ErrorMsg = AuthException.getMessage();
          System.out.println("ex: " +ErrorMsg);
          response.setStatus(500);
          response.getWriter().write(ErrorMsg);
          // response.sendError(500, ErrorMsg);
      }));
    return http.build();
  }
  @Bean
  public CorsConfigurationSource corsConfigurationSource (){
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.setAllowedHeaders(Arrays.asList("Authorization", "content-type"));
    config.setAllowedMethods(Arrays.asList("POST", "GET", "DELETE", "PATCH", "PUT"));
    config.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }
  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
    authenticationManagerBuilder.userDetailsService(customUserDetailService);
    return authenticationManagerBuilder.build();
  }
  @Bean 
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }
  
  
  
}
