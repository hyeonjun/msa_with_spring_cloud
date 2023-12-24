package com.example.orderservice.repository;

import static com.example.orderservice.entity.QUserOrder.userOrder;

import com.example.orderservice.controller.dto.PagingDTO;
import com.example.orderservice.entity.UserOrder;
import com.example.orderservice.service.vo.QUserOrderVO;
import com.example.orderservice.service.vo.UserOrderVO;
import com.example.orderservice.util.CustomQuerydslRepositorySupport;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class UserOrderRepositoryImpl extends CustomQuerydslRepositorySupport
  implements UserOrderRepositoryCustom {

  public UserOrderRepositoryImpl() {
    super(UserOrder.class);
  }

  @Override
  public Page<UserOrderVO> findAll(PagingDTO dto) {
    JPAQuery<UserOrderVO> query = select(getUserOrderVO())
      .from(userOrder)
      .orderBy(userOrder.createdAt.desc());

    JPAQuery<Long> countQuery = select(userOrder.count())
      .from(userOrder);

    return applyPagination(dto.getPageRequest(), query, countQuery);
  }

  @Override
  public Page<UserOrderVO> findAllByUserId(String userId, Pageable pageable) {
    JPAQuery<UserOrderVO> query = select(getUserOrderVO())
      .from(userOrder)
      .where(userOrder.userId.eq(userId))
      .orderBy(userOrder.createdAt.desc());

    JPAQuery<Long> countQuery = select(userOrder.count())
      .from(userOrder)
      .where(userOrder.userId.eq(userId));

    return applyPagination(pageable, query, countQuery);
  }

  private QUserOrderVO getUserOrderVO() {
    return new QUserOrderVO(
      userOrder.productId,
      userOrder.qty,
      userOrder.unitPrice,
      userOrder.totalPrice,
      userOrder.userId,
      userOrder.orderId
    );
  }

}
