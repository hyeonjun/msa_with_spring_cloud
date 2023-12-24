package com.example.userservice.application.user.controller;

import static com.example.userservice.application.user.controller.UserCmController.USER_COMMON_CONTROLLER;
import static com.example.userservice.util.ApplicationStaticResource.AUTHORITY_USER_URL;

import com.example.userservice.application.auth.service.vo.LoginUser;
import com.example.userservice.application.user.service.UserService;
import com.example.userservice.application.user.service.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(USER_COMMON_CONTROLLER)
@RequiredArgsConstructor
public class UserCmController {

  public static final String USER_COMMON_CONTROLLER = AUTHORITY_USER_URL+"/users";

  private final UserService userService;

  @GetMapping
  public ResponseEntity<UserVO> getCurrentUser(@AuthenticationPrincipal LoginUser loginUser) {
    UserVO vo = userService.getCurrentUser(loginUser);
    return new ResponseEntity<>(vo, HttpStatus.OK);
  }

}
