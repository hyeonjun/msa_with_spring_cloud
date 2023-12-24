package com.example.userservice.application.auth.service;

import com.example.userservice.application.auth.controller.dto.CreateUserDTO;
import com.example.userservice.application.auth.controller.dto.LoginUserDTO;
import com.example.userservice.application.auth.controller.dto.TokenReissueDTO;
import com.example.userservice.application.auth.entity.RefreshToken;
import com.example.userservice.application.auth.repository.RefreshTokenRepository;
import com.example.userservice.application.auth.service.vo.LoginUser;
import com.example.userservice.application.auth.service.vo.TokenVO;
import com.example.userservice.application.user.entity.User;
import com.example.userservice.application.user.repository.UserRepository;
import com.example.userservice.application.user.service.vo.UserVO;
import com.example.userservice.config.security.jwt.JwtProvider;
import com.example.userservice.util.UuidProvider;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final UserRepository userRepository;
  private final RefreshTokenRepository refreshTokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtProvider jwtProvider;

  @Transactional
  public UserVO createUser(CreateUserDTO dto) {
    log.info("Request User Info: {}", dto.toString());

    dto.setPassword(passwordEncoder.encode(dto.getPassword()));
    User user = new User(dto, UuidProvider.generateUuid());

    userRepository.save(user);
    return UserVO.valueOf(user);
  }

  @Transactional
  public TokenVO login(LoginUserDTO dto) {
    Authentication authentication = authenticationManagerBuilder.getObject()
      .authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);

    LoginUser loginUser = (LoginUser) authentication.getPrincipal();
    TokenVO vo = jwtProvider.createToken(loginUser);

    RefreshToken refreshToken = RefreshToken
      .createRefreshToken()
      .userId(loginUser.getUserId())
      .refreshToken(vo.getRefreshToken())
      .build();
    refreshTokenRepository.save(refreshToken);

    return vo;
  }

  /**
    When a user log-in, the user is given a new access token and a new refresh token.
    However, if the access token is expired and the refresh token is not expired.
    Reissue access tokens using refresh tokens
    So the access token is new and the refresh token is persisted.
  */
  public TokenVO tokenReissue(TokenReissueDTO dto) {
    jwtProvider.validateToken(dto.getRefreshToken());
    LoginUser loginUser = (LoginUser) jwtProvider.getAuthentication(
      dto.getAccessToken()).getPrincipal();

    RefreshToken refreshToken = refreshTokenRepository
      .findById(loginUser.getUserId())
      .filter(token -> token.validate(dto.getRefreshToken()))
      .orElseThrow(() -> new RuntimeException("invalid refresh token"));

    String newAccessToken = jwtProvider.getAccessToken(loginUser, new Date());
    return TokenVO.valueOf(loginUser.getUserId(), newAccessToken, refreshToken.getRefreshToken());
  }

}
