package com.banchango.factory.request;

import com.banchango.domain.users.UserType;
import com.banchango.users.dto.UserSignupRequestDto;

public class UserSignupRequestFactory {
    private static final String NAME = "NAME";
    private static final String PASSWORD = "PASSWORD";
    private static final String PHONE_NUMBER = "010-0000-0100";
    private static final String TELEPHONE_NUMBER = "02-0000-0100";
    private static final UserType TYPE = UserType.OWNER;
    private static final String COMPANY_NAME = "COMPANY_NAME";

    private static int countUsers = 0;

    public static UserSignupRequestDto createNewUser() {
        return create(generateEmail());
    }

    public static UserSignupRequestDto createNewUser(String email) {
        return create(email);
    }

    public static UserSignupRequestDto createDuplicateUser(String duplicateEmail) {
        return create(duplicateEmail);
    }

    private static UserSignupRequestDto create(String email) {
        return UserSignupRequestDto.builder()
            .companyName(COMPANY_NAME)
            .email(email)
            .name(NAME)
            .password(PASSWORD)
            .telephoneNumber(TELEPHONE_NUMBER)
            .phoneNumber(PHONE_NUMBER)
            .type(TYPE)
            .build();
    }
    private static String generateEmail() {
        return "EMAIL"+(countUsers++);
    }
}
