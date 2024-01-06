package com.example.voting_system.web;

import com.example.voting_system.error.AppException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@RestControllerAdvice
@AllArgsConstructor
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private final ErrorAttributes errorAttributes;

    @ExceptionHandler(AppException.class)
    public ResponseEntity<Map<String, Object>> appException(AppException ex, WebRequest request) {
        log.error("Application Exception", ex);
        Map<String, Object> body = errorAttributes.getErrorAttributes(request, ex.getOptions());
        body.put("status", ex.getStatusCode());
        body.put("error",ex.getReason());
        return ResponseEntity.status(ex.getStatusCode()).body(body);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        log.error("Exception", ex);
        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }
}
