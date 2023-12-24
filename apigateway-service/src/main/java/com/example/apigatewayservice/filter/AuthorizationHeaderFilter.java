package com.example.apigatewayservice.filter;

import static com.example.apigatewayservice.code.UserAuthority.AU;

import com.example.apigatewayservice.code.UserAuthority;
import com.example.apigatewayservice.code.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Objects;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * GatewayFilter는 GatewayFilterFactory를 구현해야하고 추상 클래스인 AbstractGatewayFilterFactory를 상속받아
 * 구현해 사용한다. GatewayFilter apply 메서드를 오버라이딩하면 exchage 라는 요청/응답을 갖는 변수를 받을 수 있다.
 * 우리는 요청이 들어온 후에 헤더의 JWT 토큰 값을 판단해 검증을 진행할 것이고, 성공적으로 검증되었을 경우 토큰 헤더를
 * 실제 필요한 정보(사용자 아이디(userId))를 추출하여 마이크로 서비스로 전달해주려고 한다.
 * 만약 검증 실패 시 ErrorWebExceptionHandler를 이용하여 예외를 잡아 에러코드를 던져줄 것이다.
 */
@Slf4j
@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

  private final SecretKey key;

  public AuthorizationHeaderFilter(@Value("${token.secret}") String secretKey) {
    super(Config.class);
    this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
  }

  public static class Config {
    // Put configuration properties here
  }

  @Override
  public GatewayFilter apply(Config config) {
    return ((exchange, chain) -> {
      ServerHttpRequest request = exchange.getRequest();
      String uri = request.getPath().value();
      log.info("path info: {}, uri method: {}", uri, request.getMethod());
      String authority = uri.substring(1).split("/")[2];

      if (AU.getValue().equals(authority)) { // GUEST
        return chain.filter(exchange);
      }

      if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
        return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
      }

      String authorizerHeader = Objects.requireNonNull(
        request.getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);

      Claims claims = getValidParseClaims(
        authorizerHeader.replace("Bearer ", ""));

      String userId = claims.get("userId", String.class);
      String userRole = getAuthorities(claims);
      log.info("userId: {}, userRole: {}", userId, userRole);

      if (!UserRole.of(userRole).isAvailable(UserAuthority.of(authority))) {
        return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
      }

      // 성공적으로 검증이 되었기 때문에 인증된 헤더로 요청을 변경해준다.
      // 서비스는 해당 헤더에서 아이디를 가져와 사용한다.
      request.mutate()
        .header("X-Authorization-Id", userId, userRole)
        .build();

      return chain.filter(exchange);
    });
  }

  private Claims getValidParseClaims(String jwt) {
    try {
      return getJwtParseSignedClaims(jwt);
    } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
      throw new RuntimeException("wrong jwt signature");
    } catch (ExpiredJwtException e) {
      throw new RuntimeException("expired jwt token");
    } catch (UnsupportedJwtException e) {
      throw new RuntimeException("unsupported jwt token");
    } catch (IllegalArgumentException e) {
      throw new RuntimeException("wrong jwt token");
    }
  }

  private Claims getJwtParseSignedClaims(String accessToken) {
    return Jwts.parser()
      .verifyWith(key)
      .build()
      .parseSignedClaims(accessToken).getPayload();
  }

  private String getAuthorities(Claims claims) {
    String[] authorities = claims.get("role").toString().split(",");
    if (!(authorities.length == 1)) {
      throw new RuntimeException("invalid user role.(user must have only one role)");
    }
    return authorities[0];
  }

  // Mono, Flux -> Spring WebFlux
  private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
    ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(httpStatus);

    log.error(err);
    return response.setComplete();
  }
}
