package com.example.userservice.config.security.session;

import static com.example.userservice.application.user.entity.code.Role.SYSTEM;
import static com.example.userservice.application.user.entity.code.Role.USER;
import static com.example.userservice.util.ApplicationStaticResource.AUTHORITY_AUTH_URL;
import static com.example.userservice.util.ApplicationStaticResource.AUTHORITY_SYSTEM_URL;
import static com.example.userservice.util.ApplicationStaticResource.AUTHORITY_USER_URL;
import static com.example.userservice.util.ApplicationStaticResource.COMMON_URL;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Slf4j
//@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  public static final String SECURITY_SIGN_IN_URL = COMMON_URL+"/sign-in";
  public static final String SECURITY_SIGN_OUT_URL = COMMON_URL+"/sign-out";
  private final CustomAuthenticationProvider authenticationProvider;
  private final RestAuthenticationEntryPoint authenticationEntryPoint;
  private final RestSuccessHandler restSuccessHandler;
  private final RestFailureHandler restFailureHandler;
  private final RestAccessDeniedHandler restAccessDeniedHandler;
  private final LogoutSuccessHandler logoutSuccessHandler;

  /**
   * 특정 자원에 대해서 인증된 사용자의 접근에 대해 인가하기 위한 설정
   * 해당 자원에 대해서는 Security를 적용하지 않음
   * @return WebSecurityCustomizer
   */
  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring().antMatchers();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      // 서버에 인증 정보를 저장하지 않기에 csrf를 사용하지 않음
      .csrf().disable()
      .cors().disable()
      // form 기반 로그인에 대해 비활성화
      .formLogin().disable()
      .exceptionHandling()
      .authenticationEntryPoint(authenticationEntryPoint)
      .accessDeniedHandler(restAccessDeniedHandler)
      .and()
      .logout()
      .logoutRequestMatcher(new AntPathRequestMatcher(SECURITY_SIGN_OUT_URL))
      .logoutSuccessHandler(logoutSuccessHandler)
      .invalidateHttpSession(true)
      .and()
      // 리소스 접근에 대한 인가에 대한 설정
      .authorizeRequests()
      .antMatchers(AUTHORITY_SYSTEM_URL+"/**").hasRole(SYSTEM.name())
      .antMatchers(AUTHORITY_USER_URL+"/**").hasRole(USER.name())
      .antMatchers(SECURITY_SIGN_IN_URL, AUTHORITY_AUTH_URL+"/**").permitAll()
      .antMatchers(SECURITY_SIGN_OUT_URL).authenticated()
//      .antMatchers("/**").permitAll()
      .anyRequest().denyAll()
      .and()
      .sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
      .maximumSessions(1)
      .maxSessionsPreventsLogin(true);

    AuthenticationManager authenticationManager =
      authenticationManager(http.getSharedObject(AuthenticationConfiguration.class));
    CustomUsernamePasswordAuthenticationFilter authenticationFilter =
      getAuthenticationFilter(authenticationManager);

    AuthenticationManagerBuilder authenticationManagerBuilder =
      http.getSharedObject(AuthenticationManagerBuilder.class);
    authenticationManagerBuilder.authenticationProvider(authenticationProvider);

    http.addFilterAt(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(
    AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public CustomUsernamePasswordAuthenticationFilter getAuthenticationFilter(
    AuthenticationManager authenticationManager) {
    CustomUsernamePasswordAuthenticationFilter filter =
      new CustomUsernamePasswordAuthenticationFilter();

    try {
      filter.setFilterProcessesUrl(SECURITY_SIGN_IN_URL);
      filter.setUsernameParameter("email");
      filter.setPasswordParameter("password");
      filter.setAuthenticationManager(authenticationManager);
      filter.setAuthenticationSuccessHandler(restSuccessHandler);
      filter.setAuthenticationFailureHandler(restFailureHandler);
      filter.afterPropertiesSet();
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return filter;
  }
}
