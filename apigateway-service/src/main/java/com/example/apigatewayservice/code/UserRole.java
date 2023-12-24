package com.example.apigatewayservice.code;

import static com.example.apigatewayservice.code.UserAuthority.AU;
import static com.example.apigatewayservice.code.UserAuthority.CM;
import static com.example.apigatewayservice.code.UserAuthority.SY;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum UserRole {

  SYSTEM(new UserAuthority[] {SY, CM, AU}),
  USER(new UserAuthority[] {CM, AU}),
  GUEST(new UserAuthority[] {AU});

  private final UserAuthority[] authorities;

  UserRole(UserAuthority[] authorities) {
    this.authorities = authorities;
  }

  public static UserRole of(String role) {
    return Arrays.stream(values())
      .filter(v -> role.equals(v.name()))
      .findFirst()
      .orElseThrow(() -> new RuntimeException(
        String.format("user role %s is invalid", role)));
  }

  public boolean isAvailable(UserAuthority authority) {
    return Arrays.asList(this.getAuthorities())
      .contains(authority);
  }
}
