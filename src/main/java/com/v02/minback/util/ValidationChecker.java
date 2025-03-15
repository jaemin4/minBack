package com.v02.minback.util;

import com.v02.minback.exception.BankRuntimeException;
import com.v02.minback.exception.UserRuntimeException;
import com.v02.minback.model.param.BankAccountDepositParam;
import com.v02.minback.model.param.BankAccountTransferParam;
import com.v02.minback.model.param.BankAccountWithdrawParam;
import com.v02.minback.model.param.UserAccountSaveParam;

public class ValidationChecker {

    public static void userAccountSaveValidationCheck(UserAccountSaveParam param){
        if(param.getBalance() == null || param.getEmail() == null || param.getPassword() == null || param.getName() == null){
            throw new UserRuntimeException("회원가입 실패") ;
        }
    }
    public static void depositValidationCheck(BankAccountDepositParam param) {
        if(param.getBalance() == null || param.getAccountNumber() == null){
            throw new BankRuntimeException("입금 실패 : 계좌 번호가 없습니다.");
        }

    }

    public static void withrdrawValidationCheck(BankAccountWithdrawParam param) {
        if(param.getBalance() == null || param.getAccountNumber() == null){
            throw new BankRuntimeException("출금 실패 : 계좌 번호가 없습니다.");
        }

    }

    public static void transferValidationCheck(BankAccountTransferParam param) {
        if (param.getBalance() == null || param.getFromAccountNumber() == null || param.getToAccountNumber() == null) {
            throw new BankRuntimeException("이체 실패 : 계좌 번호가 없습니다.");
        }

    }
}
