package com.v02.minback.model.result;


import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtTokenResponse {

    private String accessToken;
    private Cookie refreshCookie;
}
