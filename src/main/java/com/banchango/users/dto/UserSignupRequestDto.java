package com.banchango.users.dto;

import com.banchango.common.validator.ValueOfEnum;
import com.banchango.domain.users.UserRole;
import com.banchango.domain.users.UserType;
import com.banchango.domain.users.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

    @NotNull(message = "Type is required.(SHIPPER or OWNER)")
    @ValueOfEnum(enumClass = UserType.class)
    private UserType type;

    @NotBlank(message = "Company Name field is required.")
    private String companyName;

    @NotBlank(message = "Telephone Number is required.")
    private String telephoneNumber;

    @NotBlank(message = "Phone Number is required.")
    private String phoneNumber;

    public User toEntity() {
        return User.builder()
                .name(name).email(email)
                .password(password).type(type)
                .telephoneNumber(telephoneNumber)
                .phoneNumber(phoneNumber)
                .companyName(companyName)
                .role(UserRole.USER)
                .build();
    }

    @Builder
    public UserSignupRequestDto(String name, String companyName, String email, String password, String telephoneNumber, String phoneNumber, UserType type) {
        this.companyName = companyName;
        this.email = email;
        this.name = name;
        this.password = password;
        this.telephoneNumber = telephoneNumber;
        this.phoneNumber = phoneNumber;
        this.type = type;
    }
}
