package com.banchango.users.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserSigninResponseDto {
    private UserInfoResponseDto User;
    private String accessToken;
    private String tokenType;
    private String refreshToken;
}
