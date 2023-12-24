package com.example.catalogservice.service.vo;

import com.example.catalogservice.entity.Catalog;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
public class CatalogVO {

  private String productId;
  private String productName;
  private Integer unitPrice;
  private Integer stock;
  private LocalDateTime createdAt;

  public static CatalogVO valueOf(Catalog catalog) {
    CatalogVO vo = new CatalogVO();
    vo.setProductId(catalog.getProductId());
    vo.setProductName(catalog.getProductName());
    vo.setUnitPrice(catalog.getUnitPrice());
    vo.setStock(catalog.getStock());
    vo.setCreatedAt(catalog.getCreatedAt());
    return vo;
  }

  @QueryProjection
  public CatalogVO(String productId, String productName, Integer unitPrice, Integer stock,
    LocalDateTime createdAt) {
    this.productId = productId;
    this.productName = productName;
    this.unitPrice = unitPrice;
    this.stock = stock;
    this.createdAt = createdAt;
  }
}
