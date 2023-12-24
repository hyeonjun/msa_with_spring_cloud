package com.example.userservice.application.auth.repository;

import com.example.userservice.application.auth.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

}
