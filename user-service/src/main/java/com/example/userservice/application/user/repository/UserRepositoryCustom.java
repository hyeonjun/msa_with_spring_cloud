package com.example.userservice.application.user.repository;

import com.example.userservice.application.common.dto.PagingDTO;
import com.example.userservice.application.user.service.vo.UserVO;
import org.springframework.data.domain.Page;

public interface UserRepositoryCustom {

  Page<UserVO> findAll(PagingDTO dto);
}
