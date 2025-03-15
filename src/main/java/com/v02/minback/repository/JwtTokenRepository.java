package com.v02.minback.repository;


import com.v02.minback.model.entity.AuthJwtEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JwtTokenRepository extends JpaRepository<AuthJwtEntity,Long> {
    Optional<AuthJwtEntity> findByRefreshToken(String refreshToken);
    void deleteByRefreshToken(String refreshToken);

    Optional<AuthJwtEntity> findByAccessToken(String access);
}
