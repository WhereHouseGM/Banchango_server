package com.banchango.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ChangePasswordRequestDto {
    @NotBlank(message = "기존 비밀번호 값이 주어지지 않았습니다")
    @Size(min = 64, max = 64)
    private String originalPassword;

    @NotBlank(message = "새로운 비밀번호 값이 주어지지 않았습니다")
    @Size(min = 64, max = 64)
    private String newPassword;
}
