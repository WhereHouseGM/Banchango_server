package com.banchango.users.exception;

import com.banchango.common.exception.NoContentException;

public class UserNotFoundException extends NoContentException{

    private static final long serialVersionUID = 1L;
    public static final String MESSAGE = "이메일 또는 비밀번호가 잘못되었습니다.";

    public UserNotFoundException() {
        super(MESSAGE);
    }
}
