package com.example.catalogservice.controller;

import static com.example.catalogservice.util.ApplicationStaticResource.AUTHORIZATION_HEADER_VALUE;

import com.example.catalogservice.controller.dto.CreateCatalogDTO;
import com.example.catalogservice.controller.dto.PagingDTO;
import com.example.catalogservice.service.CatalogService;
import com.example.catalogservice.service.vo.CatalogVO;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CatalogController {

  private final Environment env;
  private final CatalogService catalogService;

  @PostMapping("/sy/catalogs")
  public ResponseEntity<CatalogVO> createCatalog(
    @RequestBody @Valid CreateCatalogDTO dto) {
    CatalogVO vo = catalogService.createCatalog(dto);
    return new ResponseEntity<>(vo, HttpStatus.CREATED);
  }

  @GetMapping("/au/catalogs")
  public ResponseEntity<Page<CatalogVO>> getCatalogs(@Valid PagingDTO dto) {
    Page<CatalogVO> result = catalogService.getCatalogs(dto);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/health-check")
  public String healthCheck() {
    return String.format("It's Working in Catalog Service on Port %s",
      env.getProperty("local.server.port"));
  }

}
