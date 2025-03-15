package com.v02.minback.repository;

import com.v02.minback.model.entity.RedisJwtEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RedisJwtRepository extends CrudRepository<RedisJwtEntity, String> {
    Optional<RedisJwtEntity> findByAccessToken(String accessToken);
}
