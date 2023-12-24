package com.example.orderservice.service;

import com.example.orderservice.controller.dto.CreateUserOrderDTO;
import com.example.orderservice.controller.dto.PagingDTO;
import com.example.orderservice.entity.UserOrder;
import com.example.orderservice.repository.UserOrderRepository;
import com.example.orderservice.service.vo.UserOrderVO;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserOrderService {

  private final UserOrderRepository userOrderRepository;

  @Transactional
  public UserOrderVO createOrder(CreateUserOrderDTO dto, String userId) {
    UserOrder userOrder = UserOrder.create(dto, userId);

    while (true) {
      String uuid = generatedOrderUUID();
      if (Objects.nonNull(uuid)) {
        userOrder.setOrderId(uuid);
        break;
      }
    }

    userOrderRepository.save(userOrder);
    return UserOrderVO.valueOf(userOrder);
  }

  public UserOrderVO getOrderByOrderId(String orderId, String userId) {
    UserOrder userOrder = userOrderRepository.findByOrderId(orderId)
      .orElseThrow(() -> new RuntimeException(orderId + "is not found"));

    if (!userOrder.getUserId().equals(userId)) {
      throw new RuntimeException("NOT AUTHORIZATION");
    }

    return UserOrderVO.valueOf(userOrder);
  }

  public Page<UserOrderVO> getOrders(PagingDTO dto) {
    return userOrderRepository.findAll(dto);
  }

  public Page<UserOrderVO> getOrdersByUserId(String userId, PagingDTO dto) {
    return userOrderRepository.findAllByUserId(userId, dto.getPageRequest());
  }

  private String generatedOrderUUID() {
    String uuid = UUID.randomUUID().toString();
    if (userOrderRepository.findByOrderId(uuid).isPresent()) {
      return null;
    }
    return uuid;
  }
}
