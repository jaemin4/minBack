package com.v02.minback.model.param;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankAccountWithdrawParam {
    @Positive(message = "계좌번호 필수")
    private Long accountNumber;

    @Positive(message = "금액 필수")
    private Long balance;

}
