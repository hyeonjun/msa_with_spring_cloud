package com.example.userservice.application.auth.service.vo;

import java.util.Collections;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Getter
public class LoginUser extends User {

  private final Long id;
  private final String userId;
  private final String email;
  private final String name;

  public LoginUser(com.example.userservice.application.user.entity.User user) {
    super(user.getEmail(), user.getPassword(),
      Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())));
    this.id = user.getId();
    this.userId = user.getUserId();
    this.email = user.getEmail();
    this.name = user.getName();
  }
}
