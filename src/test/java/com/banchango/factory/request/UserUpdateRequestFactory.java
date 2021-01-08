package com.banchango.factory.request;

import com.banchango.users.dto.UserUpdateRequestDto;

public class UserUpdateRequestFactory {
    public static final String NEW_NAME = "NEW_NAME";
    public static final String NEW_COMP_NAME = "NEW_COMP_NAME";
    public static final String NEW_TELEPHONE_NUMBER = "02000000";
    public static final String NEW_PHONE_NUMBER = "010000000";

    public static UserUpdateRequestDto create() {
        return UserUpdateRequestDto.builder()
                .name(NEW_NAME)
                .companyName(NEW_COMP_NAME)
                .telephoneNumber(NEW_TELEPHONE_NUMBER)
                .phoneNumber(NEW_PHONE_NUMBER)
                .build();
    }
}
