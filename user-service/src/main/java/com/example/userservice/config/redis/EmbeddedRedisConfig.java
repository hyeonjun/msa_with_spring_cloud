//package com.example.userservice.config.redis;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.Objects;
//import javax.annotation.PostConstruct;
//import javax.annotation.PreDestroy;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.util.StringUtils;
//import redis.embedded.RedisServer;
//
//@Slf4j
//@Configuration
//public class EmbeddedRedisConfig {
//
//  @Value("${spring.redis.port}")
//  private int redisPort;
//
//  private RedisServer redisServer;
//
//  @PostConstruct
//  public void startRedis() throws IOException {
//    int port = isRedisRunning() ? findAvailablePort() : redisPort;
//    redisServer = RedisServer.builder()
//      .port(port)
//      .setting("maxmemory 128M")
//      .build();
//    redisServer.start();
//  }
//
//  @PreDestroy
//  public void stopRedis() {
//    redisServer.stop();
//  }
//
//  public int findAvailablePort() throws IOException {
//    for (int port = 10000; port <= 65535; port++) {
//      Process process = executeGrepProcessCommand(port);
//      if (!isRunning(process)) {
//        return port;
//      }
//    }
//
//    throw new RuntimeException("not found available port");
//  }
//
//  /**
//   * Embedded Redis가 현재 실행중인지 확인
//   */
//  private boolean isRedisRunning() throws IOException {
//    return isRunning(executeGrepProcessCommand(redisPort));
//  }
//
//  /**
//   * 해당 port를 사용중인 프로세스를 확인하는 sh 실행
//   */
//  private Process executeGrepProcessCommand(int redisPort) throws IOException {
//    String command = String.format("netstat -nat | grep LISTEN|grep %d", redisPort);
//    String[] shell = {"/bin/sh", "-c", command};
//
//    return Runtime.getRuntime().exec(shell);
//  }
//
//  /**
//   * 해당 Process가 현재 실행중인지 확인
//   */
//  private boolean isRunning(Process process) {
//    String line;
//    StringBuilder pidInfo = new StringBuilder();
//
//    try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
//      while ((line = input.readLine()) != null) {
//        pidInfo.append(line);
//      }
//    } catch (Exception e) {
//      throw new RuntimeException("error executing embedded redis");
//    }
//    return StringUtils.hasText(pidInfo.toString());
//  }
//
//}
