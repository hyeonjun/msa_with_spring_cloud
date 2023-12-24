package com.example.catalogservice.entity;

import com.example.catalogservice.controller.dto.CreateCatalogDTO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Catalog extends Base {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Setter
  @Column(nullable = false, length = 121, unique = true)
  private String productId;
  @Column(nullable = false)
  private String productName;
  @Column(nullable = false)
  private Integer stock;
  @Column(nullable = false)
  private Integer unitPrice;

  public static Catalog create(CreateCatalogDTO dto) {
    Catalog catalog = new Catalog();
    catalog.productName = dto.getProductName();
    catalog.stock = dto.getStock();
    catalog.unitPrice = dto.getUnitPrice();
    return catalog;
  }
}
