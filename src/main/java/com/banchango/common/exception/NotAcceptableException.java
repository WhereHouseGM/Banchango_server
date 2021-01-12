package com.banchango.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class NotAcceptableException extends ApiException{
    public NotAcceptableException(String message) {
        super(message, HttpStatus.NOT_ACCEPTABLE);
    }

    public NotAcceptableException() {
        this("Not Acceptable Exception.");
    }
}
