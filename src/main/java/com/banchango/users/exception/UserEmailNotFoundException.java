package com.banchango.users.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserEmailNotFoundException extends UserException{

    private static final long serialVersionUID = 1L;
    public static final String MESSAGE = "해당 이메일은 가입되어 있지 않습니다.";

    public UserEmailNotFoundException() {
        super(MESSAGE);
    }
}
