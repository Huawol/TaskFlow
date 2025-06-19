package com.example.taskflow.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

//회원탈퇴 요청 시 클라이언트가 비밀번호를 전달하기 위한 DTO
@Getter
@NoArgsConstructor
public class UserDeleteRequestDto {

	private String password;

}