package com.pro.service.front;

import com.pro.model.entity.RefreshEntity;
import com.pro.model.result.TokenResponseDto;
import com.pro.repository.RefreshTokenRepository;
import com.pro.service.persist.JwtService;
import com.pro.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

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
