package com.v02.minback.repository;

import com.v02.minback.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,String> {

    Optional<UserEntity> findByEmail(String username);



}
