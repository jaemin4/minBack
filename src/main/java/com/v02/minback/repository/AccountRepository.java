package com.v02.minback.repository;

import com.v02.minback.model.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity,Long> {
    Optional<AccountEntity> findByAccountNumber(Long accountNumber);



}
