package com.v02.minback.service.persist;

import com.v02.minback.model.entity.UserEntity;
import com.v02.minback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    public UserEntity saveOauthUser(UserEntity userEntity){
        return userRepository.save(userEntity);
    }
    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }



}
