package com.banchango.users.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public abstract class UserException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public UserException(String message){super(message);}
}
