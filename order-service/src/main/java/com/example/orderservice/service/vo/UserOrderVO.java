package com.example.orderservice.service.vo;

import com.example.orderservice.entity.UserOrder;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
public class UserOrderVO {

  private String productId;
  private Integer qty;
  private Integer unitPrice;
  private Integer totalPrice;
  private String userId;
  private String orderId;

  public static UserOrderVO valueOf(UserOrder userOrder) {
    UserOrderVO vo = new UserOrderVO();
    vo.setProductId(userOrder.getProductId());
    vo.setQty(userOrder.getQty());
    vo.setUnitPrice(userOrder.getUnitPrice());
    vo.setTotalPrice(userOrder.getTotalPrice());
    vo.setUserId(userOrder.getUserId());
    vo.setOrderId(userOrder.getOrderId());
    return vo;
  }

  @QueryProjection
  public UserOrderVO(String productId, Integer qty, Integer unitPrice, Integer totalPrice,
    String userId,
    String orderId) {
    this.productId = productId;
    this.qty = qty;
    this.unitPrice = unitPrice;
    this.totalPrice = totalPrice;
    this.userId = userId;
    this.orderId = orderId;
  }
}
