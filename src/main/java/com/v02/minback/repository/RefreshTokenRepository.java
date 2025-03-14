package com.v02.minback.repository;


import com.v02.minback.model.entity.RefreshEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshEntity,Long> {
    Optional<RefreshEntity> findByRefreshToken(String refreshToken);
    void deleteByRefreshToken(String refreshToken);

    Optional<RefreshEntity> findByAccessToken(String access);
}
