package com.v02.minback.service.persist;

import com.v02.minback.model.entity.RefreshEntity;
import com.v02.minback.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    private final RefreshTokenRepository refreshTokenRepository;

    public void saveJwtToken(RefreshEntity refreshEntity){
        RefreshEntity resultRefreshEntity = refreshTokenRepository.save(refreshEntity);

        log.info("JWT 저장 완료 : {}",resultRefreshEntity);
    }

    public RefreshEntity findByRefreshName(String refresh){
        return refreshTokenRepository.findByRefreshToken(refresh).orElse(null);

    }
    @Transactional
    public void deleteAuth(String refresh){
        refreshTokenRepository.deleteByRefreshToken(refresh);
    }

    public RefreshEntity findByAccessToken(String access){
        return refreshTokenRepository.findByAccessToken(access).orElse(null);

    }


}
