package com.example.catalogservice.entity;

import java.time.LocalDateTime;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@MappedSuperclass
public class Base {

  @CreationTimestamp
  private LocalDateTime createdAt;
  @UpdateTimestamp
  private LocalDateTime modifiedAt;
}

