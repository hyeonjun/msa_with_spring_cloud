package com.example.userservice.application.user.service;

import com.example.userservice.application.auth.service.vo.LoginUser;
import com.example.userservice.application.common.dto.PagingDTO;
import com.example.userservice.application.user.entity.User;
import com.example.userservice.application.user.repository.UserRepository;
import com.example.userservice.application.user.service.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public UserVO getCurrentUser(LoginUser loginUser) {
    User user = userRepository.findByUserId(loginUser.getUserId())
      .orElseThrow(() -> new RuntimeException("not found user"));
    return UserVO.valueOf(user);
  }

  public Page<UserVO> getUsers(PagingDTO dto) {
    return userRepository.findAll(dto);
  }

  public UserVO getUser(String userId) {
    User user = userRepository.findByUserId(userId)
      .orElseThrow(() -> new RuntimeException("not found user"));

    return UserVO.valueOf(user);
  }

}
