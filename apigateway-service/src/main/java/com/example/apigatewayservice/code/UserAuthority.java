package com.example.apigatewayservice.code;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum UserAuthority {

  SY("sy"),
  CM("cm"),
  AU("au");

  private final String value;

  UserAuthority(String value) {
    this.value = value;
  }

  public static UserAuthority of(String auth) {
    return Arrays.stream(values())
      .filter(v -> v.getValue().equals(auth))
      .findFirst()
      .orElseThrow(() -> new RuntimeException(
        String.format("user authoity %s is invalid", auth)));
  }
}
