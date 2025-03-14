package com.v02.minback.service.front;

import com.v02.minback.model.entity.RefreshEntity;
import com.v02.minback.service.persist.JwtService;
import com.v02.minback.util.JwtUtil;
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
