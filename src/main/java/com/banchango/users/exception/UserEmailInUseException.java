package com.banchango.users.exception;

import com.banchango.common.exception.ConflictException;

public class UserEmailInUseException extends ConflictException{

    public static final String MESSAGE = "해당 이메일은 이미 사용 중 입니다.";

    public UserEmailInUseException() {
        super(MESSAGE);
    }
}
