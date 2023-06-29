package com.example.server.model;




import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateNameRequestBody {


  @NotNull(message = "Id must not be empty")
  private Integer id;

  @NotBlank(message = "Name must not be empty")
  @Size(min = 3, max = 20, message = "Name must between 3 and 10")
  private String name;
}
