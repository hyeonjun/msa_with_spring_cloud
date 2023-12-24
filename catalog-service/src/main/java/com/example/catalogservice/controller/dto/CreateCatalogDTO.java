package com.example.catalogservice.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCatalogDTO {

  private String productName;
  private Integer stock;
  private Integer unitPrice;
}
