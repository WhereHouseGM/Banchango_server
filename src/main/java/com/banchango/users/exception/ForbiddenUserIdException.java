package com.banchango.users.exception;

import com.banchango.common.exception.ForbiddenException;

public class ForbiddenUserIdException extends ForbiddenException {
    private static final String MESSAGE = "해당 유저 id에 요청을 보낼 수 있는 권한이 없습니다";

    public ForbiddenUserIdException(String message) {
        super(message);
    }
    
    public ForbiddenUserIdException() {
        this(MESSAGE);
    }
}
