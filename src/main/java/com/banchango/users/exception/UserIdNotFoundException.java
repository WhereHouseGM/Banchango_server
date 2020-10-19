package com.banchango.users.exception;

public class UserIdNotFoundException extends UserException{

    private static final long serialVersionUID = 1L;
    public static final String MESSAGE = "해당 id로 조회된 결과가 없습니다.";

    public UserIdNotFoundException() {
        super(MESSAGE);
    }
}
