package com.v02.minback.controller;

import com.v02.minback.model.param.BankAccountDepositParam;
import com.v02.minback.model.param.BankAccountTransferParam;
import com.v02.minback.model.param.BankAccountWithdrawParam;
import com.v02.minback.model.param.UserAccountSaveParam;
import com.v02.minback.model.result.RestResult;
import com.v02.minback.resolver.CurrentUser;
import com.v02.minback.service.bank.front.BankFrontService;
import com.v02.minback.util.Utils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/user/bank")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankFrontService bankFrontService;
    private final Utils utils;

    @Operation(summary = "회원가입", description = "Bank 사용자 회원가입을 진행합니다.")
    @PostMapping("/save")
    public RestResult save(@Valid @RequestBody UserAccountSaveParam param){

        return bankFrontService.saveUserAccount(param);
    }

    @Operation(summary = "입금", description = "입금을 진행합니다.")
    @PostMapping("/deposit")
    public RestResult deposit(@Valid @RequestBody BankAccountDepositParam param, @CurrentUser Authentication authentication) {
        log.info("/deposit : {}", utils.toJson(param));
        log.info("현재 사용자 : {}",utils.toJson(authentication));

        return bankFrontService.deposit(param);
    }

    @Operation(summary = "출금", description = "출금을 진행합니다.")
    @PostMapping("/withdraw")
    public RestResult withdraw(@Valid @RequestBody BankAccountWithdrawParam param){
        return bankFrontService.withdraw(param);
    }

    @Operation(summary = "이체", description = "이체를 진행합니다.")
    @PostMapping("/transfer")
    public RestResult transfer(@Valid @RequestBody BankAccountTransferParam param){
        return bankFrontService.transfer(param);
    }

}
