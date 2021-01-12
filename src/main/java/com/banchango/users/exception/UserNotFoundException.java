package com.banchango.users.exception;

import com.banchango.common.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {

    public static final String MESSAGE = "이메일 또는 비밀번호가 잘못되었습니다.";

    public UserNotFoundException() {
        super(MESSAGE);
    }
}
