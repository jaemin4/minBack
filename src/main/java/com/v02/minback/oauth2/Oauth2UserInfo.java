package com.v02.minback.oauth2;

public interface Oauth2UserInfo {
    String getProvider();
    String getProviderId();
    String getEmail();
    String getName();
}
