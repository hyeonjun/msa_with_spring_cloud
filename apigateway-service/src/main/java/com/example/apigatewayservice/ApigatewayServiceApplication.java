package com.example.apigatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApigatewayServiceApplication {

  /**
   * Spring Cloud Gateway 는 API Gateway로서 클라이언트의 요청을
   * 엔드포인트에 따라 알맞는 마이크로 서비스로 라우팅해준다.
   * 모든 요청들이 SCG를 거치기 때문에 여러 마이크로서비스에서 사용하는 토큰 값 검증 등과 같이
   * 인증 및 보안 보안 관련 설정을 SCG에서 공통적으로 처리해주는 것이 가능
   * SCG의 가장 큰 특징은 Tomcat이 아닌 Non-Blocking, Asynchronous 이벤트 기반의 WAS인 Netty를 사용한다.
   *  - Non-Blocking: 네트워크 통신이 완료되는 것을 기다리지 않고 바로 다음 작업을 수행. 성능이나 효율성이 뛰어남
   *  - Asynchronous: 동시에 일어나지 않는 것을 의미, 요청 받은 함수가 작업을 마치면 요청자에게 콜백을 통해 알려줌
   *  API Gateway는 모든 요청이 게이트웨이를 통해 들어오기 때문에 성능적인 이슈에 민감하다.
   *  SCG는 이러한 성능적인 이슈를 줄이기 위해 복잡하지만 성능을 높일 수 있는 Netty를 채택한 것으로 보임
   */

  public static void main(String[] args) {
    SpringApplication.run(ApigatewayServiceApplication.class, args);
  }

}
