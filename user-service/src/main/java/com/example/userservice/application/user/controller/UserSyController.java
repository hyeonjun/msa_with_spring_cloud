package com.example.userservice.application.user.controller;

import static com.example.userservice.application.user.controller.UserSyController.USER_SYSTEM_CONTROLLER;
import static com.example.userservice.util.ApplicationStaticResource.AUTHORITY_SYSTEM_URL;

import com.example.userservice.application.common.dto.PagingDTO;
import com.example.userservice.application.user.service.UserService;
import com.example.userservice.application.user.service.vo.UserVO;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(USER_SYSTEM_CONTROLLER)
@RequiredArgsConstructor
public class UserSyController {

  public static final String USER_SYSTEM_CONTROLLER = AUTHORITY_SYSTEM_URL+"/users";

  private final Environment env;
  private final UserService userService;

  @Value("${greeting.message}")
  private String welcomeMessage;

  @GetMapping
  public ResponseEntity<Page<UserVO>> getUsers(@Valid PagingDTO dto) {
    Page<UserVO> result = userService.getUsers(dto);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/{user-id}")
  public ResponseEntity<UserVO> getUser(@PathVariable(name = "user-id") String userId) {
    UserVO vo = userService.getUser(userId);
    return new ResponseEntity<>(vo, HttpStatus.OK);
  }

  @GetMapping("/health-check")
  public String healthCheck(HttpServletRequest request) {
    return String.format("It's Working in User Service on Port %s", request.getServerPort());
  }

  @GetMapping("/welcome1")
  public String welcome1() {
    return env.getProperty("greeting.message");
  }

  @GetMapping("/welcome2")
  public String welcome2() {
    return welcomeMessage;
  }

}
