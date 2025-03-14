package com.pro.repository;


import com.pro.model.entity.RefreshEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshEntity,Long> {
    Optional<RefreshEntity> findByRefreshToken(String refreshToken);
    void deleteByRefreshToken(String refreshToken);

    Optional<RefreshEntity> findByAccessToken(String access);
}
