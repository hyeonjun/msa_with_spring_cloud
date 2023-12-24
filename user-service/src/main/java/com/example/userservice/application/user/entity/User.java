package com.example.userservice.application.user.entity;

import com.example.userservice.application.auth.controller.dto.CreateUserDTO;
import com.example.userservice.application.common.entity.Base;
import com.example.userservice.application.user.entity.code.Role;
import io.jsonwebtoken.Claims;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@DynamicUpdate
public class User extends Base {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false, length = 50, unique = true)
  private String email; // 로그인에 사용할 아이디
  @Column(nullable = false, length = 50)
  private String name;
  @Column(nullable = false, unique = true)
  private String userId;

  @Setter
  @Column(nullable = false)
  private String password;

  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false)
  private Role role;

  public User(Claims claims) {
    this.id = claims.get("id", Long.class);
    this.email = claims.get("email", String.class);
    this.name = claims.get("name", String.class);
    this.userId = claims.get("userId", String.class);
    this.role = Role.of(claims.get("role", String.class));
    this.password = "";
  }

  public User(CreateUserDTO dto, String uuid) {
    this.email = dto.getEmail();
    this.name = dto.getName();
    this.userId = uuid;
    this.password = dto.getPassword();
    this.role = Role.USER;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof User)) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(getId(), user.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
