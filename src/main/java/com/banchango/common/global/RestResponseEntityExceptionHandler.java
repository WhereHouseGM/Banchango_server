package com.banchango.common.global;

import com.banchango.common.dto.ErrorResponseDto;
import com.banchango.common.exception.ApiException;
import com.banchango.tools.DateConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final ObjectMapper objectMapper;

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<Object> handleApiException(Exception exception, WebRequest request) {
        if(exception instanceof ApiException) {
            ApiException apiException = (ApiException) exception;
            return handleExceptionInternal(exception, null, new HttpHeaders(), apiException.getHttpStatus(), request);
        } else {
            return handleExceptionInternal(exception, null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
        }
    }

    @SneakyThrows
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponseDto errorResponseDto;
        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        ContentCachingRequestWrapper requestWrapper = (ContentCachingRequestWrapper) servletWebRequest.getRequest();
        if(status.equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
            logger.error("TIME : " + DateConverter.convertDateWithTime(LocalDateTime.now()));
            logger.error("REQUEST BODY : " + objectMapper.readTree(requestWrapper.getContentAsByteArray()));
            logger.error("PATH: " + servletWebRequest.getRequest().getRequestURI());
            logger.error("REMOTE ADDR: " + servletWebRequest.getRequest().getRemoteAddr());
            errorResponseDto = new ErrorResponseDto(new Date(), status.value(), status.getReasonPhrase(), "", servletWebRequest.getRequest().getRequestURI());
        } else {
            errorResponseDto = new ErrorResponseDto(new Date(), status.value(), status.getReasonPhrase(), ex.getMessage(), servletWebRequest.getRequest().getRequestURI());
        }
        return new ResponseEntity<>(errorResponseDto, headers, status);
    }
}
