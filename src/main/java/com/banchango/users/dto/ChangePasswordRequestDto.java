package com.banchango.users.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class ChangePasswordRequestDto {
    @NotBlank(message = "새로운 비밀번호 값이 주어지지 않았습니다")
    private String password;
}
