package com.pro.filter;

import com.pro.model.entity.RefreshEntity;
import com.pro.service.front.JwtFrontService;
import com.pro.util.FilterUtil;
import com.pro.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class SecurityJwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final FilterUtil filterUtil;
    private final JwtFrontService jwtFrontService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        RefreshEntity authResult =
                jwtFrontService.getByAccess(request.getHeader("Authorization"));

        if(authResult == null){
            filterChain.doFilter(request,response);
            return;
        }

        String accessToken = authResult.getAccessToken();

        try {
            jwtUtil.isExpired(accessToken);
        }catch (ExpiredJwtException e){

            log.warn("access token expired");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        jwtUtil.isAccessCategory(jwtUtil.getCategory(accessToken));

        filterUtil.setContextHodler(
           jwtUtil.getUsername(accessToken), jwtUtil.getRole(accessToken)
        );

        filterChain.doFilter(request,response);
    }
}
