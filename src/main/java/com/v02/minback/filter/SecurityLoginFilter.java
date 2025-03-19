package com.v02.minback.filter;

import com.v02.minback.model.result.JwtTokenResponse;
import com.v02.minback.service.auth.front.JwtFrontService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class SecurityLoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtFrontService jwtFrontService;

    public SecurityLoginFilter(AuthenticationManager authenticationManager,JwtFrontService jwtFrontService) {
        this.authenticationManager = authenticationManager;
        this.jwtFrontService = jwtFrontService;
        setFilterProcessesUrl("/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                obtainUsername(request),
                obtainPassword(request),
                null
        );
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        JwtTokenResponse jwtTokenResponse = jwtFrontService.saveJwtToken(authResult);
        SecurityContextHolder.getContext().setAuthentication(authResult);

        response.setHeader("access", jwtTokenResponse.getAccessToken());
        response.addCookie(jwtTokenResponse.getRefreshCookie());
        response.setStatus(HttpStatus.OK.value());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
    }



}
