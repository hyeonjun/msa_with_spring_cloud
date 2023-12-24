package com.example.catalogservice.repository;

import static com.example.catalogservice.entity.QCatalog.catalog;

import com.example.catalogservice.controller.dto.PagingDTO;
import com.example.catalogservice.entity.Catalog;
import com.example.catalogservice.service.vo.CatalogVO;
import com.example.catalogservice.service.vo.QCatalogVO;
import com.example.catalogservice.util.CustomQuerydslRepositorySupport;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;

public class CatalogRepositoryImpl extends CustomQuerydslRepositorySupport
  implements CatalogRepositoryCustom {

  public CatalogRepositoryImpl() {
    super(Catalog.class);
  }

  @Override
  public Page<CatalogVO> findAll(PagingDTO dto) {
    JPAQuery<CatalogVO> query = select(getCatalogVO())
      .from(catalog)
      .orderBy(catalog.createdAt.desc());

    JPAQuery<Long> countQuery = select(catalog.count())
      .from(catalog);

    return applyPagination(dto.getPageRequest(), query, countQuery);
  }

  private QCatalogVO getCatalogVO() {
    return new QCatalogVO(
      catalog.productId,
      catalog.productName,
      catalog.unitPrice,
      catalog.stock,
      catalog.createdAt
    );
  }
}
