package com.example.orderservice.repository;

import com.example.orderservice.controller.dto.PagingDTO;
import com.example.orderservice.service.vo.UserOrderVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserOrderRepositoryCustom {

  Page<UserOrderVO> findAll(PagingDTO dto);
  Page<UserOrderVO> findAllByUserId(String userId, Pageable pageable);
}
