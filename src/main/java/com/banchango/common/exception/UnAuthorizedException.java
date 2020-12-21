package com.banchango.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnAuthorizedException extends ApiException {
    public UnAuthorizedException(String message) {
        super(message);
    }

    public UnAuthorizedException() {
        this("Unauthorized Exception");
    }
}
