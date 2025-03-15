package com.v02.minback.service.persist;

import com.v02.minback.model.entity.AuthJwtEntity;
import com.v02.minback.model.entity.RedisJwtEntity;
import com.v02.minback.repository.JwtTokenRepository;
import com.v02.minback.repository.RedisJwtRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtTokenRepository jwtTokenRepository;
    private final RedisJwtRepository redisJwtRepository;

    @Transactional
    public AuthJwtEntity saveJwtToken(AuthJwtEntity authJwtEntity){
        AuthJwtEntity resultAuthJwtEntity = jwtTokenRepository.save(authJwtEntity);

        log.info("JWT 저장 완료 : {}", resultAuthJwtEntity);
        return resultAuthJwtEntity;
    }

    @Transactional
    public void saveRedisJwtToken(RedisJwtEntity redisJwtEntity){
        redisJwtRepository.save(redisJwtEntity);
    }

    @Transactional
    public void deleteByRefreshToken(String refresh){
        jwtTokenRepository.deleteByRefreshToken(refresh);
    }

    public AuthJwtEntity findByAccessToken(String accessToken){
        return jwtTokenRepository.findByAccessToken(accessToken).orElse(null);
    }

    public AuthJwtEntity findByRefreshToken(String refreshToken){
        return jwtTokenRepository.findByRefreshToken(refreshToken).orElse(null);
    }


}
