package com.banchango.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class NoContentException extends ApiException{
    public NoContentException(String message) {
        super(message);
    }

    public NoContentException() {
        this("No Content Exception.");
    }
}
