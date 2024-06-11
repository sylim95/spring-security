package com.task.error;

import com.task.common.code.ReturnCode;
import com.task.error.exception.BusinessException;
import com.task.error.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;

import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    // NOTE: javax.validation.Valid or @Validated 으로 binding error 발생
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("handleMethodArgumentNotValidException, {}", ExceptionUtils.getMessage(e));
        final ErrorResponse response = ErrorResponse.of(ReturnCode.ERROR_400, e.getBindingResult());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ReturnCode.ERROR_400.getCode()));
    }

    @ExceptionHandler(value = BindException.class)
    protected ResponseEntity<ErrorResponse> handleBindException(BindException e) {
        log.error("handleBindException, {}", ExceptionUtils.getMessage(e));
        final ErrorResponse response = ErrorResponse.of(ReturnCode.ERROR_400, e.getBindingResult());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ReturnCode.ERROR_400.getCode()));
    }

    // NOTE: 지원하지 않은 HTTP method 호출
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("handleHttpRequestMethodNotSupportedException, {}", ExceptionUtils.getMessage(e));
        final ErrorResponse response = ErrorResponse.of(ReturnCode.ERROR_405);
        return new ResponseEntity<>(response, HttpStatus.valueOf(ReturnCode.ERROR_405.getCode()));
    }

    // NOTE: 지원하지 않은 URL 호출
    @ExceptionHandler(NoResourceFoundException.class)
    protected ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException e) {
        log.error("handleNoResourceFoundException, {}", ExceptionUtils.getMessage(e));
        final ErrorResponse response = ErrorResponse.of(ReturnCode.ERROR_404);
        return new ResponseEntity<>(response, HttpStatus.valueOf(ReturnCode.ERROR_404.getCode()));
    }

    // NOTE: Authentication 객체가 필요한 권한을 보유하지 않은 경우
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        log.error("handleAccessDeniedException, {}", ExceptionUtils.getMessage(e));
        final ErrorResponse response = ErrorResponse.of(ReturnCode.ERROR_403);
        return new ResponseEntity<>(response, HttpStatus.valueOf(ReturnCode.ERROR_403.getCode()));
    }

    // NOTE: HTTP 응답 에러
    @ExceptionHandler(ResponseStatusException.class)
    protected ResponseEntity<Object> handleResponseStatusException(ResponseStatusException e) {
        log.error("handleResponseStatusException, {}", ExceptionUtils.getMessage(e));
        final ErrorResponse response = ErrorResponse.of(ReturnCode.getCode(e.getStatusCode().value()));
        return new ResponseEntity<>(response, e.getStatusCode());
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException e) {
        log.error("handleBusinessException, {}", ExceptionUtils.getMessage(e));
        final ReturnCode errorCode = e.getReturnCode();
        final List<ErrorResponse.Error> errors = ErrorResponse.Error.of(StringUtils.defaultString(e.getMessage()));
        final ErrorResponse response = ErrorResponse.of(errorCode, errors);
        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getCode()));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("handleException, {}", ExceptionUtils.getMessage(e));
        final List<ErrorResponse.Error> errors = ErrorResponse.Error.of(StringUtils.defaultString(e.getMessage()));
        final ErrorResponse response = ErrorResponse.of(ReturnCode.ERROR_500, errors);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
