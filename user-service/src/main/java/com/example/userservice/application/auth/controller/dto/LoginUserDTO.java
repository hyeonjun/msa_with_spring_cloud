package com.example.userservice.application.auth.controller.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserDTO {

  @NotBlank
  private String email;
  @NotBlank
  private String password;

}
