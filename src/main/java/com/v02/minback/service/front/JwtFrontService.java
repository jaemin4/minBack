package com.v02.minback.service.front;

import com.v02.minback.exception.AuthRuntimeException;
import com.v02.minback.model.entity.AuthJwtEntity;
import com.v02.minback.model.entity.RedisJwtEntity;
import com.v02.minback.model.result.JwtTokenResponseDto;
import com.v02.minback.service.persist.JwtService;
import com.v02.minback.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtFrontService {

    private final JwtUtil jwtUtil;
    private final JwtService jwtService;

    //Redis 저장 및 DB 저장
    public JwtTokenResponseDto saveJwtToken(Authentication authResult){
        String username = authResult.getName();
        String role = authResult.getAuthorities().iterator().next().getAuthority();
        String accessToken = jwtUtil.createJwt("access", username, role, jwtUtil.getAccessExpiredTime());
        String refreshToken = jwtUtil.createJwt("refresh", username, role, jwtUtil.getRefreshExpiredTime());
        Cookie refreshCookie = jwtUtil.createCookie(refreshToken);

        AuthJwtEntity authJwtEntity = new AuthJwtEntity(
                username,
                refreshToken,
                jwtUtil.getRefreshExpiredTime(),
                accessToken,
                jwtUtil.getAccessExpiredTime()
        );

        AuthJwtEntity DbJwtResult = jwtService.saveJwtToken(authJwtEntity);

        RedisJwtEntity redisJwtEntity = new RedisJwtEntity(
                DbJwtResult.getEmail(),
                DbJwtResult.getRefreshToken(),
                DbJwtResult.getRefreshExpiration(),
                DbJwtResult.getAccessToken(),
                DbJwtResult.getAccessExpiration(),
                LocalDateTime.now(),
               null
        );

        jwtService.saveRedisJwtToken(redisJwtEntity);


        return new JwtTokenResponseDto(accessToken,refreshCookie);
    }

    //먼저 redis에서 조회 후 없으면 DB에서 조회
    @Cacheable(value = "jwtToken", key = "#accessToken", unless = "#result == null")
    public RedisJwtEntity JwtValidationByAccessToken(String accessToken) {
        // DB에서 조회
        AuthJwtEntity dbJwt = jwtService.findByAccessToken(accessToken);

        log.info("Jwt Db조회 : {}",dbJwt);

        if (dbJwt == null) {
            log.warn("접속자 정보를 찾을 수 없습니다.");
            return null;
        }

        // DB 데이터를 Redis 엔티티로 변환 후 캐싱
        RedisJwtEntity redisJwt = toRedisJwtEntity(dbJwt);
        jwtService.saveRedisJwtToken(redisJwt);

        return redisJwt;
    }

    public AuthJwtEntity reissueJwtToken(String refresh){
        if(jwtService.findByRefreshToken(refresh) == null){
            throw new AuthRuntimeException("RefreshToken Invalid");
        }
        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        jwtService.deleteByRefreshToken(refresh);

        AuthJwtEntity authJwtEntity = new AuthJwtEntity(
                username,
                jwtUtil.createJwt("refresh",username,role,jwtUtil.getRefreshExpiredTime()),
                jwtUtil.getRefreshExpiredTime(),
                jwtUtil.createJwt("access",username,role,jwtUtil.getAccessExpiredTime()),
                jwtUtil.getAccessExpiredTime()
        );

        return jwtService.saveJwtToken(authJwtEntity);
    }

    private RedisJwtEntity toRedisJwtEntity(AuthJwtEntity dbJwt) {
        return new RedisJwtEntity(
                dbJwt.getEmail(),
                dbJwt.getRefreshToken(),
                dbJwt.getRefreshExpiration(),
                dbJwt.getAccessToken(),
                dbJwt.getAccessExpiration()
        );
    }

    public AuthJwtEntity findByRefreshToken(String refreshToken){

        return jwtService.findByRefreshToken(refreshToken);
    }

    public Cookie deleteAuth(String accessToken){
        jwtService.deleteByRefreshToken(accessToken);
        return jwtUtil.createLogoutCookie();
    }

}
