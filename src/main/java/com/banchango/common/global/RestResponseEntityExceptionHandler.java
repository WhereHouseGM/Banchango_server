package com.banchango.common.global;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler{
    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    protected ResponseEntity<Object> handleBadRequest(RuntimeException exception, WebRequest request) {
        HashMap<String, Object> body = new HashMap<>();
        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        exception.printStackTrace();
        body.put("timestamp", new Date());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        body.put("message", exception.getMessage());
        body.put("path", servletWebRequest.getRequest().getRequestURI());

        return handleExceptionInternal(exception, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        HashMap<String, Object> _body = new HashMap<>();
        ServletWebRequest servletWebRequest = (ServletWebRequest) request;

        _body.put("timestamp", new Date());
        _body.put("status", status.value());
        _body.put("error", status.getReasonPhrase());
        _body.put("message", ex.getMessage());
        _body.put("path", servletWebRequest.getRequest().getRequestURI());

        return super.handleExceptionInternal(ex, _body, headers, status, request);
    }
}
