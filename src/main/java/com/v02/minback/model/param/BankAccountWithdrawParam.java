package com.v02.minback.model.param;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankAccountWithdrawParam {
    private Long accountNumber;
    private Long balance;

}
