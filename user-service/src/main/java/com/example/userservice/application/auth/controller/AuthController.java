package com.example.userservice.application.auth.controller;

import static com.example.userservice.application.auth.controller.AuthController.USER_AUTH_CONTROLLER;
import static com.example.userservice.util.ApplicationStaticResource.AUTHORITY_AUTH_URL;

import com.example.userservice.application.auth.service.AuthService;
import com.example.userservice.application.auth.service.vo.TokenVO;
import com.example.userservice.application.auth.controller.dto.CreateUserDTO;
import com.example.userservice.application.auth.controller.dto.LoginUserDTO;
import com.example.userservice.application.auth.controller.dto.TokenReissueDTO;
import com.example.userservice.application.user.service.vo.UserVO;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(USER_AUTH_CONTROLLER)
@RequiredArgsConstructor
public class AuthController {

  public static final String USER_AUTH_CONTROLLER = AUTHORITY_AUTH_URL+"/users";

  private final AuthService authService;
  private final Environment env;

  @PostMapping
  public ResponseEntity<UserVO> createUser(@RequestBody @Valid CreateUserDTO dto) {
    UserVO vo = authService.createUser(dto);
    return new ResponseEntity<>(vo, HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public ResponseEntity<TokenVO> login(@RequestBody LoginUserDTO dto) {
    TokenVO vo = authService.login(dto);
    return new ResponseEntity<>(vo, HttpStatus.OK);
  }

  @PostMapping("/reissue:token")
  public ResponseEntity<TokenVO> tokenReissue(@RequestBody TokenReissueDTO dto) {
    TokenVO vo = authService.tokenReissue(dto);
    return new ResponseEntity<>(vo, HttpStatus.OK);
  }

  @GetMapping("/health-check")
  public String status(HttpServletRequest request) {
    return String.format("User Service"
      + "\n- port(local.server.port) = " + env.getProperty("local.server.port")
      + "\n- port(server.port) = " + env.getProperty("server.port")
      + "\n- jwt token secret = " + env.getProperty("token.secret")
      + "\n- access token live time = " + env.getProperty("token.access.expiration_time")
      + "\n- refresh token live time = " + env.getProperty("token.refresh.expiration_time"));
  }

}
