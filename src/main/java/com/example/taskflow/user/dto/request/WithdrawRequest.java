package com.example.taskflow.user.dto.request;

import lombok.Getter;
import lombok.Setter;

//회원탈퇴 요청 DTO
@Getter
@Setter
public class WithdrawRequest {
    private String password;
}
