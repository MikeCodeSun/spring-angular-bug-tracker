package com.example.server.validator;

import org.springframework.beans.factory.annotation.Autowired;
import com.example.server.service.CustomUserDetailService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueValidator implements ConstraintValidator<Unique, String> {
  
  @Autowired CustomUserDetailService customUserDetailService;
  

  @Override
  public void initialize(Unique constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(String email, ConstraintValidatorContext context) {
    if(customUserDetailService != null && customUserDetailService.isUserExistByEmail(email)) {
      return false;
    }
    return true;
  }
}
