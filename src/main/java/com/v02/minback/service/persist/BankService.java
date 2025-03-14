package com.pro.service.persist;

import com.pro.exception.BankRuntimeException;
import com.pro.model.param.*;
import com.pro.repository.AccountRepository;
import com.pro.repository.BankAccountRepository;
import com.pro.repository.UserRepository;
import com.pro.model.entity.AccountEntity;
import com.pro.model.entity.BankAccountEntity;
import com.pro.model.entity.UserEntity;
import com.pro.model.result.RestResult;
import com.pro.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.LinkedHashMap;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class BankService {
    private final BankAccountRepository bankAccountRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final Utils utils;

    public RestResult getAll() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("data", bankAccountRepository.findAll());

        return new RestResult("전체 조회 성공", "success", data);
    }


    @Transactional
    public void userSave(UserEntity userEntity){
        UserEntity resultEntity = userRepository.save(userEntity);

        log.info("t_user 저장 성공 : {}", utils.toJson(resultEntity));
    }


    @Transactional
    public void deposit(BankAccountDepositParam param, AccountEntity savedAccount) {
        savedAccount.setBalance(savedAccount.getBalance() + param.getBalance());
        AccountEntity updatedAccount = accountRepository.save(savedAccount);

        log.info("입금 성공 : {} {}",utils.toJson(savedAccount),utils.toJson(updatedAccount));
    }

    @Transactional
    public void withdraw(BankAccountWithdrawParam param, AccountEntity savedAccount) {
       savedAccount.setBalance(savedAccount.getBalance() - param.getBalance());
       AccountEntity updatedAccount = accountRepository.save(savedAccount);

       log.info("출금 성공 : {} {}", utils.toJson(savedAccount), utils.toJson(updatedAccount));

    }

    public Long getNextAccountNumber() {
        return bankAccountRepository.findMaxAccountNumber();
    }

    @Transactional
    public void saveBankAccount(BankAccountEntity bankAccountEntity){
        BankAccountEntity savedEntity = bankAccountRepository.save(bankAccountEntity);

        log.info("t_bank_account 저장 성공 : {}",utils.toJson(savedEntity));
    }

    @Transactional
    public void transfer(BankAccountTransferParam param, AccountEntity fromSavedAccount, AccountEntity toSavedAccount) {
        fromSavedAccount.setBalance(fromSavedAccount.getBalance() - param.getBalance());
        toSavedAccount.setBalance(toSavedAccount.getBalance() - param.getBalance());

        AccountEntity fromUpdateAccount = accountRepository.save(fromSavedAccount );
        AccountEntity toUpdatedAccount = accountRepository.save(toSavedAccount);

        log.info("From 이체 : {},{} , To 이체 : {} {}",
                utils.toJson(fromSavedAccount),utils.toJson(fromUpdateAccount),
                utils.toJson(toSavedAccount),utils.toJson(toUpdatedAccount)
        );

    }


    public AccountEntity getBankAccount(Long accountNumber) {

        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(()-> new BankRuntimeException("해당 계좌를 찾을 수 없습니다."));
    }


}
