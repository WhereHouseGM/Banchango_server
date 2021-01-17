package com.banchango.users.exception;

import com.banchango.common.exception.NotFoundException;

public class UserEmailNotFoundException extends NotFoundException {

    public static final String MESSAGE = "해당 이메일은 가입되어 있지 않습니다.";

    public UserEmailNotFoundException() {
        super(MESSAGE);
    }
}
