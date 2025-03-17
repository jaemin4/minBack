package com.v02.minback.model.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAccountSaveParam {

    @Positive(message = "금액 필수")
    private Long balance;

    @NotBlank(message = "이름 필수")
    private String name;

    @NotBlank(message = "이메일 필수")
    private String email;

    @NotBlank(message = "패스워드 필수")
    private String password;

    private String role;

    private String userId;
}
