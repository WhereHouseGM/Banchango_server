package com.banchango.auth.exception;

public class AuthenticateException extends Exception{

    private static final long serialVersionUID = 1L;
    public static String MESSAGE = "인증에 실패했습니다.";

    public AuthenticateException() {
        super(MESSAGE);
    }

    public AuthenticateException(String message) {
        this.MESSAGE = message;
    }
}
