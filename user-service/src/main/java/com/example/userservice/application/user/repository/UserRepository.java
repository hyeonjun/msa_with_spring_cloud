package com.example.userservice.application.user.repository;

import com.example.userservice.application.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long>,
  UserRepositoryCustom {

  // TODO: 12/11/23 mysql 사용 시 email 으로 검색할 때 아래 쿼리 사용해야 함 
  @Query(value = "select u.* from user u where BINARY(u.email) = :email", nativeQuery = true)
  Optional<User> findByBinaryEmail(@Param("email") String email);
  
  Optional<User> findByEmail(String email);

  Optional<User> findByUserId(String userId);
}
