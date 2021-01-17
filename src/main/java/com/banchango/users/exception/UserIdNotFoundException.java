package com.banchango.users.exception;

import com.banchango.common.exception.NotFoundException;

public class UserIdNotFoundException extends NotFoundException {
    public static final String MESSAGE = "해당 id로 조회된 결과가 없습니다.";

    public UserIdNotFoundException() {
        super(MESSAGE);
    }
}
