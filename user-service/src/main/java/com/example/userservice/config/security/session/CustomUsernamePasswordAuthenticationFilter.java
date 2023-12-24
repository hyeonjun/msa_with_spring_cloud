package com.example.userservice.config.security.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class CustomUsernamePasswordAuthenticationFilter  extends
  UsernamePasswordAuthenticationFilter {

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
    HttpServletResponse response) throws AuthenticationException {
    return super.attemptAuthentication(request, response);
  }

  @Override
  protected String obtainPassword(HttpServletRequest request) {
    return super.obtainPassword(request);
  }

  @Override
  protected String obtainUsername(HttpServletRequest request) {
    return super.obtainUsername(request);
  }

}
