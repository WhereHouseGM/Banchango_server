package com.banchango.users.exception;

public class UserEmailInUseException extends UserException{

    private static final long serialVersionUID = 1L;
    public static final String MESSAGE = "해당 이메일은 이미 사용 중 입니다.";

    public UserEmailInUseException() {
        super(MESSAGE);
    }
}
