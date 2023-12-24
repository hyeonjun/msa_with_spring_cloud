package com.example.userservice.application.user.entity.code;

import java.util.Arrays;

public enum Role {

  SYSTEM, USER, GUEST;

  public static Role of(String name) {
    return Arrays.stream(Role.values())
      .filter(r -> r.name().equals(name))
      .findFirst()
      .orElse(GUEST);
  }
}
