package com.pro.controller;

import com.pro.model.entity.UserEntity;
import com.pro.model.param.BankAccountDepositParam;
import com.pro.model.param.UserAccountSaveParam;
import com.pro.model.param.BankAccountTransferParam;
import com.pro.model.param.BankAccountWithdrawParam;
import com.pro.resolver.CurrentUser;
import com.pro.service.front.BankFrontService;
import com.pro.service.persist.BankService;
import com.pro.model.result.RestResult;
import com.pro.util.Utils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/user/bank")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankService bankService;
    private final BankFrontService bankFrontService;
    private final Utils utils;

    @GetMapping("/getAll")
    public RestResult getAll(){
        return bankService.getAll();
    }

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
    public RestResult withdraw(@RequestBody BankAccountWithdrawParam param, HttpServletRequest request,@CurrentUser Authentication auth){
        return bankFrontService.withdraw(param);

    }

    @PostMapping("/transfer")
    public RestResult transfer(@RequestBody BankAccountTransferParam param,@CurrentUser Authentication auth){
        return bankFrontService.transfer(param);

    }


}
