package com.v02.minback.service.persist;

import com.v02.minback.exception.BankRuntimeException;
import com.v02.minback.model.entity.AccountEntity;
import com.v02.minback.model.entity.BankAccountEntity;
import com.v02.minback.model.entity.UserEntity;
import com.v02.minback.model.param.BankAccountDepositParam;
import com.v02.minback.model.param.BankAccountTransferParam;
import com.v02.minback.model.param.BankAccountWithdrawParam;
import com.v02.minback.repository.AccountRepository;
import com.v02.minback.repository.BankAccountRepository;
import com.v02.minback.repository.UserRepository;
import com.v02.minback.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Slf4j
@Service
@RequiredArgsConstructor
public class BankService {
    private final BankAccountRepository bankAccountRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final Utils utils;

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
