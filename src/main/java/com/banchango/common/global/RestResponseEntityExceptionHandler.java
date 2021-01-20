package com.banchango.common.global;

import com.banchango.common.dto.ErrorResponseDto;
import com.banchango.common.exception.ApiException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler{
    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<Object> handleApiException(Exception exception, WebRequest request) {
        if(exception instanceof ApiException) {
            ApiException apiException = (ApiException) exception;
            return handleExceptionInternal(exception, null, new HttpHeaders(), apiException.getHttpStatus(), request);
        } else {
            return handleExceptionInternal(exception, null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
        }
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponseDto errorResponseDto;
        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        if(status.equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
            errorResponseDto = new ErrorResponseDto(new Date(), status.value(), status.getReasonPhrase(), "", servletWebRequest.getRequest().getRequestURI());
        } else {
            errorResponseDto = new ErrorResponseDto(new Date(), status.value(), status.getReasonPhrase(), ex.getMessage(), servletWebRequest.getRequest().getRequestURI());
        }
        return new ResponseEntity<>(errorResponseDto, headers, status);
    }
}
