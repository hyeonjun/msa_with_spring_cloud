package com.example.catalogservice.service;

import com.example.catalogservice.controller.dto.CreateCatalogDTO;
import com.example.catalogservice.controller.dto.PagingDTO;
import com.example.catalogservice.entity.Catalog;
import com.example.catalogservice.repository.CatalogRepository;
import com.example.catalogservice.service.vo.CatalogVO;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CatalogService {

  private final CatalogRepository catalogRepository;

  @Transactional
  public CatalogVO createCatalog(CreateCatalogDTO dto) {
    Catalog catalog = Catalog.create(dto);

    while (true) {
      String uuid = generatedOrderUUID();
      if (Objects.nonNull(uuid)) {
        catalog.setProductId(uuid);
        break;
      }
    }

    catalogRepository.save(catalog);
    return CatalogVO.valueOf(catalog);
  }

  public Page<CatalogVO> getCatalogs(PagingDTO dto) {
    return catalogRepository.findAll(dto);
  }

  private String generatedOrderUUID() {
    String uuid = UUID.randomUUID().toString();
    if (catalogRepository.findByProductId(uuid).isPresent()) {
      return null;
    }
    return uuid;
  }
}
