package com.example.catalogservice.repository;

import com.example.catalogservice.controller.dto.PagingDTO;
import com.example.catalogservice.service.vo.CatalogVO;
import org.springframework.data.domain.Page;

public interface CatalogRepositoryCustom {

  Page<CatalogVO> findAll(PagingDTO dto);


}
