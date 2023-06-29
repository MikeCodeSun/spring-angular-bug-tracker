package com.example.server.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequseBody {
  
  @NotBlank(message = "Email must not be empty")
  @Email
  private String email;

  @NotBlank(message = "Password must not be empty")
  private String password;

}
