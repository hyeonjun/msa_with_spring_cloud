package com.example.userservice.application.user.service.vo;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderVO {

  private String productId;
  private Integer qty;
  private Integer unitPrice;
  private Integer totalPrice;
  private LocalDateTime createdAt;

  private String orderId;
}
