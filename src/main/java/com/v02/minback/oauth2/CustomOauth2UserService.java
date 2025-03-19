package com.v02.minback.oauth2;

import com.v02.minback.model.entity.UserEntity;
import com.v02.minback.repository.UserRepository;
import com.v02.minback.util.ServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final String defaultRole = "ROLE_ADMIN";

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("getAttributes : {}", oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getRegistrationId();
        Oauth2UserInfo oauth2UserInfo = null;

        if(provider.equals("google")){
            log.info("구글 로그인");
            oauth2UserInfo = new GoogleUserDetails(oAuth2User.getAttributes());
        }

        String providerId = oauth2UserInfo.getProviderId();
        String email = oauth2UserInfo.getEmail();
        String loginId = provider + "_" + providerId;
        String name = oauth2UserInfo.getName();


        Optional<UserEntity> findUser = userRepository.findByEmail(email);
        UserEntity userEntity = new UserEntity();

        if(findUser.isEmpty()) {
            userEntity.setUserId(ServiceUtil.createUserId());
            userEntity.setName(name);
            userEntity.setRole(defaultRole);
            userEntity.setEmail(email);
            userEntity.setPassword(null);
            userEntity.setProvider(provider);
            userEntity.setProviderId(providerId);

            userRepository.save(userEntity);
        }   else{
            userEntity = findUser.get();
        }

        return new CustomOauth2UserDetails(userEntity,oAuth2User.getAttributes());
    }
}
