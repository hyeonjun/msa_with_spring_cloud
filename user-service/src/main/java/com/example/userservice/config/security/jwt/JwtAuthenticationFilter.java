package com.example.userservice.config.security.jwt;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String BEARER_TYPE = "Bearer ";
  private final JwtProvider jwtProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
    FilterChain filterChain) throws ServletException, IOException {
    Optional.ofNullable(resolveToken(request))
      .filter(jwtProvider::validateToken)
      .map(jwtProvider::getAuthentication)
      .ifPresent(SecurityContextHolder.getContext()::setAuthentication);
    filterChain.doFilter(request, response);
  }

  private String resolveToken(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(AUTHORIZATION_HEADER))
      .filter(token -> StringUtils.hasText(token) && token.startsWith(BEARER_TYPE))
      .map(token -> token.substring(BEARER_TYPE.length()))
      .orElse(null);
  }
}
