package com.banchango.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserWithdrawRequestDto {
    @NotBlank(message = "탈퇴 사유가 주어지지 않았습니다")
    private String cause;
}
