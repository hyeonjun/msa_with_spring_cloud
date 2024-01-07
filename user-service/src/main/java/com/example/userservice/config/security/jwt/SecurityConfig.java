package com.example.userservice.config.security.jwt;

import static com.example.userservice.application.user.entity.code.Role.SYSTEM;
import static com.example.userservice.application.user.entity.code.Role.USER;
import static com.example.userservice.util.ApplicationStaticResource.ACTUATOR_ALL_URL;
import static com.example.userservice.util.ApplicationStaticResource.AUTHORITY_AUTH_URL;
import static com.example.userservice.util.ApplicationStaticResource.AUTHORITY_SYSTEM_URL;
import static com.example.userservice.util.ApplicationStaticResource.AUTHORITY_USER_URL;

import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtProvider jwtProvider;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public GrantedAuthorityDefaults grantedAuthorityDefaults() {
    return new GrantedAuthorityDefaults("");
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web
      .ignoring().antMatchers("/profile", "/manage/health", "/h2-console/**",
        "/v3/api-docs", "/swagger-ui/**", "/swagger-resources/**", "/swagger-ui.html",
        "/webjars/**", "/swagger/**", ACTUATOR_ALL_URL);
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
      .cors(AbstractHttpConfigurer::disable)
      .csrf(AbstractHttpConfigurer::disable)
      .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
      .formLogin().disable()
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()
      .exceptionHandling()
      .authenticationEntryPoint((req, res, ex) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED))
      .accessDeniedHandler((req, res, ex) -> res.sendError(HttpServletResponse.SC_FORBIDDEN))
      .and()
      .authorizeRequests()
      .antMatchers(AUTHORITY_SYSTEM_URL+"/**").hasRole(SYSTEM.name())
      .antMatchers(AUTHORITY_USER_URL+"/**").hasAnyRole(USER.name(), SYSTEM.name())
      .antMatchers(AUTHORITY_AUTH_URL+"/**").permitAll()
      .anyRequest().denyAll()
      .and()
      .build();
  }

}
