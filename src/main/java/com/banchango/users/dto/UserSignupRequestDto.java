package com.banchango.users.dto;

import com.banchango.domain.users.UserType;
import com.banchango.domain.users.Users;
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

    public Users toEntity() {
        return Users.builder()
                .name(name).email(email)
                .password(password).type(UserType.valueOf(type))
                .telephoneNumber(telephoneNumber)
                .phoneNumber(phoneNumber)
                .companyName(companyName).build();
    }
}
