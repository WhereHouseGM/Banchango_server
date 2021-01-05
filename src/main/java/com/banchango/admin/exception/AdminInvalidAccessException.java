package com.banchango.admin.exception;

import com.banchango.common.exception.UnAuthorizedException;

public class AdminInvalidAccessException extends UnAuthorizedException {
    public AdminInvalidAccessException() {
        super("해당 정보를 조회할 권한이 없습니다.");
    }
}
