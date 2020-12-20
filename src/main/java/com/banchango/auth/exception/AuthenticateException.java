package com.banchango.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AuthenticateException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    public static final String MESSAGE = "인증에 실패했습니다.";

    public AuthenticateException(String message) {
        super(MESSAGE);
    }

    public AuthenticateException() {
        this(MESSAGE);
    }
}
