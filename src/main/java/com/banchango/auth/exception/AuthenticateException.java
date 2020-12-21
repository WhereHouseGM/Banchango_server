package com.banchango.auth.exception;

import com.banchango.common.exception.UnAuthorizedException;

public class AuthenticateException extends UnAuthorizedException {
    public AuthenticateException(String message) {
        super(message);
    }

    public AuthenticateException() {
        super();
    }
}
