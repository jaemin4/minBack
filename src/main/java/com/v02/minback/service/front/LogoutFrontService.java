package com.pro.service.front;

import com.pro.model.entity.RefreshEntity;
import com.pro.service.persist.JwtService;
import com.pro.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogoutFrontService {

    private final JwtService jwtService;
    private final JwtUtil jwtUtil;


    public RefreshEntity findByRefreshName(String refresh){

        return jwtService.findByRefreshName(refresh);
    }

    public Cookie deleteAuth(String refresh){
        jwtService.deleteAuth(refresh);
        return jwtUtil.createLogoutCookie();
    }


}
