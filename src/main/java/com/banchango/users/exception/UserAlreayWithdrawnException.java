package com.banchango.users.exception;

import com.banchango.common.exception.ConflictException;

public class UserAlreayWithdrawnException extends ConflictException {
    public static final String MESSAGE = "이미 탈퇴한 사용자입니다";

    public UserAlreayWithdrawnException() {
        super(MESSAGE);
    }
}
