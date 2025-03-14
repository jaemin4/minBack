package com.v02.minback.service.front;

import com.v02.minback.model.entity.RefreshEntity;
import com.v02.minback.model.result.TokenResponseDto;
import com.v02.minback.service.persist.JwtService;
import com.v02.minback.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtFrontService {

    private final JwtUtil jwtUtil;
    private final JwtService jwtService;

    public TokenResponseDto saveToken(Authentication authResult){
        String username = authResult.getName();
        String role = authResult.getAuthorities().iterator().next().getAuthority();
        String accessToken = jwtUtil.createJwt("access", username, role, jwtUtil.getAccessExpiredTime());
        String refreshToken = jwtUtil.createJwt("refresh", username, role, jwtUtil.getRefreshExpiredTime());
        Cookie refreshCookie = jwtUtil.createCookie(refreshToken);

        RefreshEntity refreshEntity = new RefreshEntity(
                username,
                refreshToken,
                jwtUtil.getRefreshExpiredTime(),
                accessToken,
                jwtUtil.getAccessExpiredTime()
        );

        jwtService.saveJwtToken(refreshEntity);

        return new TokenResponseDto(accessToken,refreshCookie);
    }

    public RefreshEntity getByAccess(String access){
        return jwtService.findByAccessToken(access);
    }


}
