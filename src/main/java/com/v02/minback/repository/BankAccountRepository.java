package com.pro.repository;

import com.pro.model.entity.BankAccountEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccountEntity,String> {

    @Query("SELECT COALESCE(MAX(a.accountNumber), 0) FROM BankAccountEntity a")
    Long findMaxAccountNumber();

}
