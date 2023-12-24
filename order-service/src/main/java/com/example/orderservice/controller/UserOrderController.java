package com.example.orderservice.controller;

import static com.example.orderservice.util.ApplicationStaticResource.AUTHORIZATION_HEADER_VALUE;

import com.example.orderservice.controller.dto.CreateUserOrderDTO;
import com.example.orderservice.controller.dto.PagingDTO;
import com.example.orderservice.service.UserOrderService;
import com.example.orderservice.service.vo.UserOrderVO;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserOrderController {

  private final Environment env;
  private final UserOrderService userOrderService;

  @PostMapping("/cm/orders")
  public ResponseEntity<UserOrderVO> createOrder(
    @RequestBody @Valid CreateUserOrderDTO dto,
    @RequestHeader(name = AUTHORIZATION_HEADER_VALUE) String... authorization) {
    UserOrderVO vo = userOrderService.createOrder(dto, authorization[0]);
    log.info("authorization: {}", (Object) authorization);
    return new ResponseEntity<>(vo, HttpStatus.CREATED);
  }

  @GetMapping("/cm/orders")
  public ResponseEntity<Page<UserOrderVO>> getOrdersByUserId(
    @Valid PagingDTO dto,
    @RequestHeader(name = AUTHORIZATION_HEADER_VALUE) String... authorization) {
    Page<UserOrderVO> result = userOrderService.getOrdersByUserId(authorization[0], dto);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/cm/orders/{order-id}")
  public ResponseEntity<UserOrderVO> getOrderByUserId(
    @PathVariable(value = "order-id") String orderId,
    @RequestHeader(name = AUTHORIZATION_HEADER_VALUE) String... authorization) {
    UserOrderVO vo = userOrderService.getOrderByOrderId(orderId, authorization[0]);
    return new ResponseEntity<>(vo, HttpStatus.OK);
  }

  @GetMapping("/sy/orders")
  public ResponseEntity<Page<UserOrderVO>> getOrders(@Valid PagingDTO dto) {
    Page<UserOrderVO> result = userOrderService.getOrders(dto);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

}
