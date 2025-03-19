package com.v02.minback.util;


import com.v02.minback.model.details.CustomUserDetails;
import com.v02.minback.model.entity.UserEntity;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FilterUtil {

    public void setContextHodler(String username, String role){
        log.info("set : {}, role : {}", username, role);

        UserEntity userEntity = new UserEntity(
                username,
                role
        );

        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                customUserDetails,
                "",
                customUserDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public Boolean chkLogoutUrlAndMethod(HttpServletRequest request){
        return request.getRequestURI().equals("/logout") && "POST".equals(request.getMethod());
    }

    public String chkIsRefresh(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies){
            if(cookie.getName().equals("refresh")){
                return cookie.getValue();
            }
        }
        return null;
    }



}
