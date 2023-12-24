package com.example.orderservice.controller.dto;

import lombok.Data;

@Data
public class CreateUserOrderDTO {

  private String productId;
  private Integer qty;
  private Integer unitPrice;

}
