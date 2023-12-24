package com.example.userservice.config.security.session;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 사용자의 ‘인증’에 대해 실패하였을 경우 수행되는 Handler로 실패에 대한 사용자에게 반환값을 구성하여 전달
 */
@Slf4j
//@Component
public class RestFailureHandler extends SimpleUrlAuthenticationFailureHandler {

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
    AuthenticationException exception) throws IOException, ServletException {
    // application/json response
    MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
    MediaType jsonMimeType = MediaType.APPLICATION_JSON;
    ResponseEntity<String> responseData;
    if(exception instanceof BadCredentialsException) {
      String exceptionMessage = StringUtils.hasText(exception.getMessage()) ?
        exception.getMessage() : "BAD CREDENTIALS";
      responseData = new ResponseEntity<>(exceptionMessage, HttpStatus.OK);
    } else if (exception instanceof AccountExpiredException) {
      responseData = new ResponseEntity<>("inactive user", HttpStatus.OK);
    } else {
      responseData = new ResponseEntity<>("FAIL", HttpStatus.OK);
    }
    jsonConverter.write(responseData, jsonMimeType, new ServletServerHttpResponse(response));
  }
}
