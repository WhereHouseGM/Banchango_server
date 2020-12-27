package com.banchango.users.exception;

import com.banchango.common.exception.UnAuthorizedException;

public class UserInvalidAccessException extends UnAuthorizedException {
    private static final String MESSAGE = "해당 사용자의 정보를 조회할 권한이 없습니다.";
    public UserInvalidAccessException() {
        super(MESSAGE);
    }
}
