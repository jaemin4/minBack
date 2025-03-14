package com.pro.controller;

import com.pro.exception.UserRuntimeException;
import com.pro.model.result.RestResult;
import com.pro.model.entity.RefreshEntity;
import com.pro.repository.RefreshTokenRepository;
import com.pro.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/reissue")
    public RestResult accessTokenRessiue(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();

        String refresh = null;
        for(Cookie cookie : cookies){
            if(cookie.getName().equals("refresh")){
                refresh = cookie.getValue();
                break;
            }
        }

        if(refresh == null){
            throw new UserRuntimeException("refresh 토큰이 존재하지 않습니다");
        }

        try {
            jwtUtil.isExpired(refresh);
        }catch (ExpiredJwtException e){
            throw  new UserRuntimeException("토큰이 만료되었습니다");
        }

        if(!jwtUtil.getCategory(refresh).equals("refresh")){
            throw new UserRuntimeException("refresh 토큰이 존재하지 않습니다");
        }

        RefreshEntity isExistRefresh = refreshTokenRepository.findByRefreshToken(refresh)
                .orElseThrow(()-> new UserRuntimeException("토큰이 존재하지 않습니다."));

        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        String newAccess = jwtUtil.createJwt("access",username,role,jwtUtil.getAccessExpiredTime());
        String newRefresh = jwtUtil.createJwt("refresh",username,role,jwtUtil.getRefreshExpiredTime());

        refreshTokenRepository.deleteByRefreshToken(refresh);
        RefreshEntity refreshEntity = new RefreshEntity(
                username,
                refresh,
                jwtUtil.getRefreshExpiredTime(),
                newAccess,
                jwtUtil.getAccessExpiredTime()
        );

        refreshTokenRepository.save(refreshEntity);

        response.setHeader("access",newAccess);
        response.addCookie(jwtUtil.createCookie(newRefresh));
        return new RestResult("access token reissue success","success");
    }


}
