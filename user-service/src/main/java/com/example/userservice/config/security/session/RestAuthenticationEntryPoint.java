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
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
//@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
    AuthenticationException authException) throws IOException, ServletException {
    log.info("request url : {}", request.getRequestURI());

    MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
    ResponseEntity<String> responseData = new ResponseEntity<>("NOT AUTHORIZED", HttpStatus.OK);
    response.setStatus(200);
    jsonConverter.write(responseData, MediaType.APPLICATION_JSON,
      new ServletServerHttpResponse(response));
  }
}
