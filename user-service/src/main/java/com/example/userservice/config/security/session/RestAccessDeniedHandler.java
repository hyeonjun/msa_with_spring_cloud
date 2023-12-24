package com.example.userservice.config.security.session;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

//@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
    AccessDeniedException accessDeniedException) throws IOException, ServletException {
    MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
    ResponseEntity<String> responseData = new ResponseEntity<>("FAIL", HttpStatus.OK);
    response.setStatus(200);
    jsonConverter.write(responseData, MediaType.APPLICATION_JSON, new ServletServerHttpResponse(response));
  }
}
