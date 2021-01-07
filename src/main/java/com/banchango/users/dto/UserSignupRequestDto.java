package com.banchango.users.dto;

import com.banchango.common.validator.ValueOfEnum;
import com.banchango.domain.users.UserRole;
import com.banchango.domain.users.UserType;
import com.banchango.domain.users.Users;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
@Setter
public class UserSignupRequestDto {

    @NotBlank(message = "Name field is required.")
    private String name;

    @NotBlank(message = "Email field is required.")
    private String email;

    @NotBlank(message = "Password field is required.")
    private String password;

    @NotBlank(message = "Type is required.(SHIPPER or OWNER)")
    @ValueOfEnum(enumClass = UserType.class)
    private String type;

    @NotBlank(message = "Company Name field is required.")
    private String companyName;

    @NotBlank(message = "Telephone Number is required.")
    private String telephoneNumber;

    @NotBlank(message = "Phone Number is required.")
    private String phoneNumber;

    public Users toEntity() {
        return Users.builder()
                .name(name).email(email)
                .password(password).type(UserType.valueOf(type))
                .telephoneNumber(telephoneNumber)
                .phoneNumber(phoneNumber)
                .companyName(companyName)
                .role(UserRole.USER)
                .build();
    }
}
