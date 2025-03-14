package com.v02.minback.repository;

import com.v02.minback.model.entity.BankAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccountEntity,String> {

    @Query("SELECT COALESCE(MAX(a.accountNumber), 0) FROM BankAccountEntity a")
    Long findMaxAccountNumber();

}
