package com.example.userservice.application.user.repository;

import static com.example.userservice.application.user.entity.QUser.user;

import com.example.userservice.application.common.dto.PagingDTO;
import com.example.userservice.application.user.entity.User;
import com.example.userservice.application.user.service.vo.UserVO;
import com.example.userservice.application.user.service.vo.QUserVO;
import com.example.userservice.util.CustomQuerydslRepositorySupport;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;

public class UserRepositoryImpl extends CustomQuerydslRepositorySupport
  implements UserRepositoryCustom {

  public UserRepositoryImpl() {
    super(User.class);
  }

  @Override
  public Page<UserVO> findAll(PagingDTO dto) {
    JPAQuery<UserVO> query = select(getUserVO())
      .from(user)
      .orderBy(user.createdAt.desc());

    JPAQuery<Long> countQuery = select(user.count())
      .from(user);

    return applyPagination(dto.getPageRequest(), query, countQuery);
  }

  private QUserVO getUserVO() {
    return new QUserVO(
      user.userId,
      user.email,
      user.name
    );
  }

}
