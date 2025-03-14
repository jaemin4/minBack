package com.pro.filter;

import com.pro.service.front.LogoutFrontService;
import com.pro.util.FilterUtil;
import com.pro.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;


@Slf4j
@RequiredArgsConstructor
public class SecurityLogoutFilter extends GenericFilterBean {

    private final JwtUtil jwtUtil;
    private final LogoutFrontService logoutFrontService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }
    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if(!FilterUtil.chkLogoutUrlAndMethod(request)){
            filterChain.doFilter(request, response);
            return;
        }

        String refresh = FilterUtil.chkIsRefresh(request);

        if(refresh == null){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        try{
            jwtUtil.isExpired(refresh);
        }catch (ExpiredJwtException e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String category = jwtUtil.getCategory(refresh);
        if(!category.equals("refresh")){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if(logoutFrontService.findByRefreshName(refresh) == null){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Cookie cookie = logoutFrontService.deleteAuth(refresh);

        log.info("로그아웃");
        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
