package com.v02.minback.model.param;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankAccountTransferParam {

    @Positive(message = "송신자 필수")
    private Long toAccountNumber;

    @Positive(message = "수신자 필수")
    private Long fromAccountNumber;

    @Positive(message = "잔액 필수")
    private Long balance;

}
