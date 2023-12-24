package com.example.userservice.application.user.service.vo;

import com.example.userservice.application.user.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.querydsl.core.annotations.QueryProjection;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class UserVO {

  private String userId;
  private String email;
  private String name;

  private List<OrderVO> orders;

  @QueryProjection
  public UserVO(String userId, String email, String name) {
    this.userId = userId;
    this.email = email;
    this.name = name;
  }

  public static UserVO valueOf(User user) {
    UserVO vo = new UserVO();
    vo.setUserId(user.getUserId());
    vo.setEmail(user.getEmail());
    vo.setName(user.getName());
    return vo;
  }
}
