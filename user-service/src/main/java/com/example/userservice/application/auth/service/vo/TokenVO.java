package com.example.userservice.application.auth.service.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class TokenVO {

  private String userId;
  private String accessToken;
  private String refreshToken;

  public static TokenVO valueOf(String userId, String accessToken, String refreshToken) {
    TokenVO vo = new TokenVO();
    vo.setUserId(userId);
    vo.setAccessToken(accessToken);
    vo.setRefreshToken(refreshToken);
    return vo;
  }

}
