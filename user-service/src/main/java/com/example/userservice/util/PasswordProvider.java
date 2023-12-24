package com.example.userservice.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

//@Component
public class PasswordProvider {
  private final BCryptPasswordEncoder encoder;

  public PasswordProvider() {
    this.encoder = new BCryptPasswordEncoder();
  }

  public String encode(String rawPassword) {
    return encoder.encode(rawPassword);
  }

  public boolean matches(String rawPassword, String encodedPassword) {
    return encoder.matches(rawPassword, encodedPassword);
  }
}
