package com.example.userservice.application.auth.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "refreshToken", timeToLive = 31 * 60)
public class RefreshToken {

  @Id
  private String userId;
  private String refreshToken;

  @Builder(builderMethodName = "createRefreshToken")
  private RefreshToken(String userId, String refreshToken) {
    this.userId = userId;
    this.refreshToken = refreshToken;
  }

  public boolean validate(String refreshToken) {
    if (!this.refreshToken.equals(refreshToken)) {
      throw new IllegalArgumentException("invalid refreshToken");
    }
    return true;
  }
}
