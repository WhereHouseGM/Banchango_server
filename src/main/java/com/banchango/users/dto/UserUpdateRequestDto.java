package com.banchango.users.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
@Setter
public class UserUpdateRequestDto {

    @NotBlank(message = "Name is missing.")
    private String name;

    @NotBlank(message = "CompanyName is missing.")
    private String companyName;

    @NotBlank(message = "TelephoneNumber is missing.")
    private String telephoneNumber;

    @NotBlank(message = "PhoneNumber is missing.")
    private String phoneNumber;

    @Builder
    public UserUpdateRequestDto(String name, String companyName, String telephoneNumber, String phoneNumber) {
        this.name = name;
        this.companyName = companyName;
        this.telephoneNumber = telephoneNumber;
        this.phoneNumber = phoneNumber;
    }
}
