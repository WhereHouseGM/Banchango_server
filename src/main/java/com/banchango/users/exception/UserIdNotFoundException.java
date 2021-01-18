package com.banchango.users.exception;

import com.banchango.common.exception.NotFoundException;

public class UserIdNotFoundException extends NotFoundException {
    public static final String MESSAGE = "사용자가 없습니다.";

    public UserIdNotFoundException() {
        super(MESSAGE);
    }
}
