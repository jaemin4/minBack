package com.v02.minback.controller;

import com.v02.minback.model.param.BankAccountDepositParam;
import com.v02.minback.model.param.BankAccountTransferParam;
import com.v02.minback.model.param.BankAccountWithdrawParam;
import com.v02.minback.model.param.UserAccountSaveParam;
import com.v02.minback.model.result.RestResult;
import com.v02.minback.resolver.CurrentUser;
import com.v02.minback.service.front.BankFrontService;
import com.v02.minback.util.Utils;
import jakarta.servlet.http.HttpServletRequest;
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

    @PostMapping("/save")
    public RestResult save(@RequestBody UserAccountSaveParam param){
        return bankFrontService.saveUserAccount(param);
    }

    @PostMapping("/deposit")
    public RestResult deposit(@RequestBody BankAccountDepositParam param, @CurrentUser Authentication auth){
        log.info("/deposit : {}", utils.toJson(param));
        log.info("현재 사용자 : {}",utils.toJson(auth));

        return bankFrontService.deposit(param);
    }

    @PostMapping("/withdraw")
    public RestResult withdraw(@RequestBody BankAccountWithdrawParam param, HttpServletRequest request, @CurrentUser Authentication auth){
        return bankFrontService.withdraw(param);

    }

    @PostMapping("/transfer")
    public RestResult transfer(@RequestBody BankAccountTransferParam param, @CurrentUser Authentication auth){
        return bankFrontService.transfer(param);

    }


}
