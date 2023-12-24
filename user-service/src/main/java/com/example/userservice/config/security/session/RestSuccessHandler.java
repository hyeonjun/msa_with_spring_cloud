package com.example.userservice.config.security.session;

import com.example.userservice.application.auth.service.vo.LoginUser;
import java.io.IOException;
import java.util.Objects;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;

/**
 * 사용자의 ‘인증’에 대해 성공하였을 경우, 수행되는 Handler로 성공에 대한 사용자에게 반환 값을 구성하여 전달하는 클래스
 */
@Slf4j
//@Component
public class RestSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private RequestCache requestCache = new HttpSessionRequestCache();
  private final int SESSION_DURATION_TIME = 600;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
    Authentication authentication) throws ServletException, IOException {

    SavedRequest savedRequest = requestCache.getRequest(request, response);
    if (savedRequest != null) {
      requestCache.removeRequest(request, response);
    }

    clearAuthenticationAttributes(request);

    HttpSession session = request.getSession();
    session.setMaxInactiveInterval(SESSION_DURATION_TIME);

    response.setHeader("session-duration-time", String.valueOf(SESSION_DURATION_TIME));

    LoginUser user = null;
    if (SecurityContextHolder.getContext().getAuthentication() != null) {
      Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      if (principal instanceof LoginUser) {
        user = (LoginUser) principal;
      }
    }

    MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
    MediaType jsonMimeType = MediaType.APPLICATION_JSON;
    String data = "SUCCESS";
    if (Objects.isNull(user)) {
      data = "FAIL";
    }
    ResponseEntity<String> responseData = new ResponseEntity<>(data, HttpStatus.OK);
    jsonConverter.write(responseData, jsonMimeType, new ServletServerHttpResponse(response));
  }

  protected void handle(HttpServletRequest request, HttpServletResponse response,
    Authentication authentication) throws IOException, ServletException {
    SavedRequest savedRequest = requestCache.getRequest(request, response);
    if (savedRequest == null) {
      clearAuthenticationAttributes(request);
      return;
    }
    String targetUrlParam = getTargetUrlParameter();
    if (isAlwaysUseDefaultTargetUrl() || (targetUrlParam != null && StringUtils
      .hasText(request.getParameter(targetUrlParam)))) {
      requestCache.removeRequest(request, response);
      clearAuthenticationAttributes(request);
      return;
    }
    clearAuthenticationAttributes(request);
  }
}
