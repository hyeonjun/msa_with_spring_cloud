package com.example.userservice.config.security.jwt;

import static io.jsonwebtoken.Jwts.SIG.HS256;

import com.example.userservice.application.auth.service.vo.LoginUser;
import com.example.userservice.application.auth.service.vo.TokenVO;
import com.example.userservice.application.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

  private final JwtProperties jwtProperties;

  public Authentication getAuthentication(String accessToken) {
    Claims claims = getParseClaims(accessToken);
    validateToken(accessToken);
    return new UsernamePasswordAuthenticationToken(
      new LoginUser(new User(claims)),
      "",
      getAuthorities(claims)
    );
  }

  public TokenVO createToken(LoginUser loginUser) {
    Date now = new Date();

    return TokenVO.valueOf(loginUser.getUserId(),
      getAccessToken(loginUser, now),
      getRefreshToken(now));
  }

  public String getAccessToken(LoginUser loginUser, Date now) {
    String authorities = loginUser.getAuthorities()
      .stream()
      .map(GrantedAuthority::getAuthority)
      .collect(Collectors.joining(","));

    return Jwts.builder()
      .claim("id", loginUser.getId())
      .claim("email", loginUser.getEmail())
      .claim("name", loginUser.getName())
      .claim("userId", loginUser.getUserId())
      .claim("role", authorities)
      .issuedAt(now)
      .expiration(new Date(now.getTime() + jwtProperties.getAccessTokenExp()))
      .signWith(jwtProperties.getKey(), HS256)
      .compact();
  }

  public String getRefreshToken(Date now) {
    return Jwts.builder()
      .expiration(new Date(now.getTime() + jwtProperties.getRefreshTokenExp()))
      .signWith(jwtProperties.getKey(), HS256)
      .compact();
  }

  public boolean validateToken(String token) {
    try {
      getJwtParseSignedClaims(token);
      return true;
    } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
      log.info("wrong jwt signature");
    } catch (ExpiredJwtException e) {
      log.info("expired jwt token");
    } catch (UnsupportedJwtException e) {
      log.info("unsupported jwt token");
    } catch (IllegalArgumentException e) {
      log.info("wrong jwt token");
    }
    return false;
  }

  private Collection<? extends GrantedAuthority> getAuthorities(Claims claims) {
    String[] authorities = claims.get("role").toString().split(",");
    return Arrays.stream(authorities)
      .map(SimpleGrantedAuthority::new)
      .collect(Collectors.toSet());
  }

  private Claims getParseClaims(String accessToken) {
    try {
      return getJwtParseSignedClaims(accessToken).getPayload();
    } catch (Exception e) {
      return ((ExpiredJwtException) e).getClaims();
    }
  }

  private Jws<Claims> getJwtParseSignedClaims(String accessToken) {
    return Jwts.parser()
      .verifyWith(jwtProperties.getKey())
      .build()
      .parseSignedClaims(accessToken);
  }



}
