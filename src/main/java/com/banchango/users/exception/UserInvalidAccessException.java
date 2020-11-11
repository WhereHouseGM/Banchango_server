package com.banchango.users.exception;

public class UserInvalidAccessException extends UserException{

    private static final long serialVersionUID = 1L;
    public static final String MESSAGE = "해당 회원 정보를 수정하기 위한 권한이 없습니다.";

    public UserInvalidAccessException() {
        super(MESSAGE);
    }
}
