package com.v02.minback.model.param;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankAccountDepositParam {
    @NotNull(message = "계좌번호 필수")
    private Long accountNumber;

    @NotNull(message = "금액 필수")
    private Long balance;

}
