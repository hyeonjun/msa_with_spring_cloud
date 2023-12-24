package com.example.userservice.application.auth.controller.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenReissueDTO {

  @NotBlank
  private String userId;
  @NotBlank
  private String accessToken;
  @NotBlank
  private String refreshToken;

}
