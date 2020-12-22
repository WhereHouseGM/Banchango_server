package com.banchango.users.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class UserEmailSendRequestDto {

    @NotBlank(message = "Email is required.")
    private String email;
}
