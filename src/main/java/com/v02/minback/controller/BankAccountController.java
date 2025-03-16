package com.v02.minback.controller;

import com.v02.minback.model.param.BankAccountDepositParam;
import com.v02.minback.model.param.BankAccountTransferParam;
import com.v02.minback.model.param.BankAccountWithdrawParam;
import com.v02.minback.model.param.UserAccountSaveParam;
import com.v02.minback.model.result.RestResult;
import com.v02.minback.resolver.CurrentUser;
import com.v02.minback.service.front.BankFrontService;
import com.v02.minback.util.Utils;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "회원가입", description = "Bank 사용자 회원가입을 진행합니다.")
    @PostMapping("/save")
    public RestResult save(@RequestBody UserAccountSaveParam param){
        return bankFrontService.saveUserAccount(param);
    }

    @Operation(summary = "입금", description = "입금을 진행합니다.")
    @PostMapping("/deposit")
    public RestResult deposit(@RequestBody BankAccountDepositParam param, @CurrentUser Authentication auth){
        log.info("/deposit : {}", utils.toJson(param));
        log.info("현재 사용자 : {}",utils.toJson(auth));

        return bankFrontService.deposit(param);
    }

    @Operation(summary = "출금", description = "출금을 진행합니다.")
    @PostMapping("/withdraw")
    public RestResult withdraw(@RequestBody BankAccountWithdrawParam param, HttpServletRequest request, @CurrentUser Authentication auth){
        return bankFrontService.withdraw(param);

    }

    @Operation(summary = "이체", description = "이체를 진행합니다.")
    @PostMapping("/transfer")
    public RestResult transfer(@RequestBody BankAccountTransferParam param, @CurrentUser Authentication auth){
        return bankFrontService.transfer(param);

    }


}
