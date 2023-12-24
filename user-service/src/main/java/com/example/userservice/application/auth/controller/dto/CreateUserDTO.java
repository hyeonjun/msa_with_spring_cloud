package com.example.userservice.application.auth.controller.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateUserDTO {

  @Email
  @NotBlank
  private String email;

  @Size(min = 4, max = 24)
  @NotBlank
  private String password;

  @NotBlank
  @Size(min = 2)
  private String name;

}
