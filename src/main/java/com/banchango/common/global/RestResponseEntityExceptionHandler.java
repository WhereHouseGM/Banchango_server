package com.banchango.common.global;

import com.banchango.common.dto.ErrorResponseDto;
import com.banchango.common.exception.ApiException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler{
    @ExceptionHandler(value = ApiException.class)
    protected ResponseEntity<Object> handleApiException(ApiException exception, WebRequest request) {
        return handleExceptionInternal(exception, null, new HttpHeaders(), exception.getHttpStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponseDto errorResponseDto;
        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        errorResponseDto = new ErrorResponseDto(new Date(), status.value(), status.getReasonPhrase(), ex.getMessage(), servletWebRequest.getRequest().getRequestURI());
        return super.handleExceptionInternal(ex, errorResponseDto, headers, status, request);
    }
}
