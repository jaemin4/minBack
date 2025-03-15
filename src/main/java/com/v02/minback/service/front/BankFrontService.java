package com.v02.minback.service.front;

import com.v02.minback.exception.BankRuntimeException;
import com.v02.minback.model.entity.AccountEntity;
import com.v02.minback.model.entity.BankAccountEntity;
import com.v02.minback.model.entity.UserEntity;
import com.v02.minback.model.param.*;
import com.v02.minback.model.result.RestResult;
import com.v02.minback.service.persist.BankService;
import com.v02.minback.util.ServiceUtil;
import com.v02.minback.util.ValidationChecker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;

import static com.v02.minback.util.ValidationChecker.transferValidationCheck;
import static com.v02.minback.util.ValidationChecker.withrdrawValidationCheck;


@Slf4j
@Service
@RequiredArgsConstructor
public class BankFrontService {

    private final BankService bankService;
//    private final RabbitService rabbitService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public RestResult saveUserAccount(UserAccountSaveParam param){
        ValidationChecker.userAccountSaveValidationCheck(param);
        param.setRole("ROLE_ADMIN");

        String user_id = ServiceUtil.createUserId();

        UserEntity userEntity = new UserEntity(
                user_id,
                param.getName(),
                param.getRole(),
                param.getEmail(),
                bCryptPasswordEncoder.encode(param.getPassword())
        );

        bankService.userSave(userEntity);

        String bank_account_id = ServiceUtil.createBankAccountId();
        Long account_number = bankService.getNextAccountNumber();

        BankAccountEntity bankAccountEntity = new BankAccountEntity(
                bank_account_id,account_number,user_id
        );

        bankService.saveBankAccount(bankAccountEntity);


        return new RestResult("회원가입 성공", "success");
    }

    public RestResult deposit(BankAccountDepositParam param) {
        ValidationChecker.depositValidationCheck(param);

        final AccountEntity savedAccount = bankService.getBankAccount(param.getAccountNumber());
        BalanceLogParam balanceLogParam = new BalanceLogParam();

        String fullMethodName = this.getClass().getSimpleName() + "." +
                                new Object() {}.getClass().getEnclosingMethod().getName();
        balanceLogParam.setClassMethod(Map.of("currentMethod",fullMethodName));

        balanceLogParam.setSavedAccount(Map.of(
            "accountNumber",savedAccount.getAccountNumber(),
            "balance", savedAccount.getBalance()
        ));

        bankService.deposit(param, savedAccount);

        final AccountEntity updatedAccount = bankService.getBankAccount(param.getAccountNumber());

        balanceLogParam.setUpdatedAccount(Map.of(
            "account_number", updatedAccount.getAccountNumber(),
            "balance", updatedAccount.getBalance()
        ));



        //rabbitTemplate.convertAndSend("bank.exchange","bank.log.deposit", balanceLogParam);
        return new RestResult("입금 성공", "success");
    }

    public RestResult withdraw(BankAccountWithdrawParam param){
        withrdrawValidationCheck(param);

        final AccountEntity savedAccount = bankService.getBankAccount(param.getAccountNumber());

        if(savedAccount.getBalance() - param.getBalance() <= 0){
            throw new BankRuntimeException("잔액이 부족합니다");
        }

        final BalanceLogParam balanceLogParam = new BalanceLogParam();

        String fullMethodName = this.getClass().getSimpleName() + "." +
                new Object() {}.getClass().getEnclosingMethod().getName();
        balanceLogParam.setClassMethod(Map.of("currentMethod",fullMethodName));

        balanceLogParam.setSavedAccount(Map.of(
                "accountNumber",savedAccount.getAccountNumber(),
                "balance", savedAccount.getBalance()
        ));

        bankService.withdraw(param, savedAccount);

        final AccountEntity updatedAccount = bankService.getBankAccount(param.getAccountNumber());

        balanceLogParam.setUpdatedAccount(Map.of(
                "account_number", updatedAccount.getAccountNumber(),
                "balance", updatedAccount.getBalance()
        ));

        //rabbitTemplate.convertAndSend("bank.exchange","bank.log.withdraw", balanceLogParam);
        return new RestResult("출금 성공", "success");

    }

    public RestResult transfer(BankAccountTransferParam param){
        transferValidationCheck(param);

        final AccountEntity fromSavedAccount = bankService.getBankAccount(param.getFromAccountNumber());

        if(fromSavedAccount.getBalance() <= param.getBalance()){
            throw new RuntimeException("잔액이 부족합니다");
        }

        final AccountEntity toSavedAccount = bankService.getBankAccount(param.getToAccountNumber());
        final BalanceLogParam balanceLogParam = new BalanceLogParam();

        String fullMethodName = this.getClass().getSimpleName() + "." +
                new Object() {}.getClass().getEnclosingMethod().getName();
        balanceLogParam.setClassMethod(Map.of("currentMethod",fullMethodName));

        balanceLogParam.setSavedAccount(
                Map.of(
                        "fromAccountNumber",fromSavedAccount.getAccountNumber(),
                        "fromBalance",fromSavedAccount.getBalance(),
                        "toAccountNumber",toSavedAccount.getAccountNumber(),
                        "toBalance",toSavedAccount.getBalance()
                )
        );

        bankService.transfer(param,fromSavedAccount,toSavedAccount);

        //rabbitTemplate.convertAndSend("bank.exchange","bank.log.transfer", balanceLogParam);
        return new RestResult("이체 성공","success");
    }


}
