package com.example.amumal_project.api.auth;

import com.example.amumal_project.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity,Long> {
    Optional<RefreshTokenEntity> findByUserId(Long userId);
    Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);
    void deleteByUserId(Long userId);
    void deleteByRefreshToken(String refreshToken);

}
