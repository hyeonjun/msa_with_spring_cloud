package com.example.userservice.application.auth.service;

import com.example.userservice.application.auth.service.vo.LoginUser;
import com.example.userservice.application.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByEmail(username)
      .map(LoginUser::new)
      .orElseThrow(() -> new UsernameNotFoundException("user not found"));
  }
}
