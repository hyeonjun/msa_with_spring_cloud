package com.example.orderservice.entity;

import com.example.orderservice.controller.dto.CreateUserOrderDTO;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserOrder extends Base {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false, length = 120)
  private String productId;
  @Column(nullable = false)
  private Integer qty;
  @Column(nullable = false)
  private Integer unitPrice;
  @Column(nullable = false)
  private Integer totalPrice;

  @Column(nullable = false)
  private String userId;
  @Setter
  @Column(nullable = false, unique = true)
  private String orderId;

  public static UserOrder create(CreateUserOrderDTO dto, String userId) {
    UserOrder userOrder = new UserOrder();
    userOrder.productId = dto.getProductId();
    userOrder.qty = dto.getQty();
    userOrder.unitPrice = dto.getUnitPrice();
    userOrder.totalPrice = dto.getQty() * dto.getUnitPrice();
    userOrder.userId = userId;
    return userOrder;
  }

}
