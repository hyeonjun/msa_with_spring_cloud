package com.example.catalogservice.repository;

import com.example.catalogservice.entity.Catalog;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatalogRepository extends JpaRepository<Catalog, Long>,
  CatalogRepositoryCustom {

  Optional<Catalog> findByProductId(String productId);
}
