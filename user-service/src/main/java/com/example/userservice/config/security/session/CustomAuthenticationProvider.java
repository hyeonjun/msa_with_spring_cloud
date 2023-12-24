package com.example.userservice.config.security.session;

import com.example.userservice.application.auth.service.vo.LoginUser;
import com.example.userservice.application.user.entity.User;
import com.example.userservice.application.user.repository.UserRepository;
import com.example.userservice.util.PasswordProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * 전달받은 사용자의 아이디와 비밀번호를 기반으로 비즈니스 로직을 처리하여 사용자의 인증에 대해서 검증을 수행하는 클래스
 */
@Slf4j
//@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

  private final UserRepository userRepository;
  private final PasswordProvider passwordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    UsernamePasswordAuthenticationToken token =
      (UsernamePasswordAuthenticationToken) authentication;

    String email = token.getName();
    String password = token.getCredentials().toString();

    User user = userRepository.findByEmail(email)
      .orElseThrow(() -> new BadCredentialsException("id or password is invalid"));

    if (!comparePassword(password, user.getPassword())) {
      throw new BadCredentialsException("id or password is invalid");
    }

    LoginUser loginUser = new LoginUser(user);

    return new UsernamePasswordAuthenticationToken(
      loginUser, loginUser.getPassword(), loginUser.getAuthorities());
  }

  public boolean comparePassword(String inputPwd, String currentSavedPwd) {
    return passwordEncoder.matches(inputPwd, currentSavedPwd);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
