package com.banchango.users.exception;

import com.banchango.common.exception.ForbiddenException;

public class PasswordDoesNotMatchException extends ForbiddenException {
    private final static String MESSAGE = "비밀번호가 일치하지 않습니다";

    public PasswordDoesNotMatchException() {
        super(MESSAGE);
    }
}
