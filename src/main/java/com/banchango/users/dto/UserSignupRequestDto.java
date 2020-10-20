package com.banchango.users.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserSignupRequestDto {

    private String name;
    private String email;
    private String password;
    private String type;
    private String telephoneNumber;
    private String companyName;
    private String phoneNumber;
}
