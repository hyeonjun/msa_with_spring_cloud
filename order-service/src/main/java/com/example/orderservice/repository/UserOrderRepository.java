package com.example.orderservice.repository;

import com.example.orderservice.entity.UserOrder;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserOrderRepository extends JpaRepository<UserOrder, Long>,
  UserOrderRepositoryCustom {
  Optional<UserOrder> findByOrderId(String orderId);

}
