package com.banchango.users.dto;

import com.banchango.domain.users.UserRole;
import com.banchango.domain.users.UserType;
import com.banchango.domain.users.Users;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserInfoResponseDto {

    private int userId;
    private String name;
    private String email;
    private UserType type;
    private String phoneNumber;
    private String telephoneNumber;
    private String companyName;
    private UserRole role;

    public UserInfoResponseDto(Users user) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.type = user.getType();
        this.phoneNumber = user.getPhoneNumber();
        this.telephoneNumber = user.getTelephoneNumber();
        this.companyName = user.getCompanyName();
        this.role = user.getRole();
    }
}
