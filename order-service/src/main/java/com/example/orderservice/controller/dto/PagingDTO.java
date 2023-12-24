package com.example.orderservice.controller.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
public class PagingDTO {

  @Min(value = 0)
  private int pageNumber = 0;
  @Min(value = 1)
  @Max(value = 1000)
  private int rowCount = 20;

  public Pageable getPageRequest() {
    return PageRequest.of(this.pageNumber, this.rowCount);
  }

}
