package com.banchango.users.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserSigninRequestDto {

    private String email;
    private String password;
}
