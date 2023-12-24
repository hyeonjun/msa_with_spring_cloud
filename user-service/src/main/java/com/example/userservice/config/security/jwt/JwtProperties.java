package com.example.userservice.config.security.jwt;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import javax.crypto.SecretKey;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class JwtProperties {

  private final SecretKey key;
  private final Long accessTokenExp;
  private final Long refreshTokenExp;

  public JwtProperties(
    @Value("${token.secret}") String secret,
    @Value("${token.access.expiration_time}") Long accessTokenExp,
    @Value("${token.refresh.expiration_time}") Long refreshTokenExp) {
    this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    this.accessTokenExp = accessTokenExp;
    this.refreshTokenExp = refreshTokenExp;
  }
}
