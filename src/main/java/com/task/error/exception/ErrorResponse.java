package com.task.error.exception;

import com.task.common.code.ReturnCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private int code;

    private String message;

    private List<Error> errors;

    private ErrorResponse(final ReturnCode returnCode, final List<Error> errors) {
        this.code = returnCode.getCode();
        this.message = returnCode.getMessage();
        this.errors = errors;
    }

    private ErrorResponse(final ReturnCode returnCode) {
        this.code = returnCode.getCode();
        this.message = returnCode.getMessage();
        this.errors = new ArrayList<>();
    }

    public static ErrorResponse of(final ReturnCode returnCode, final BindingResult bindingResult) {
        return new ErrorResponse(returnCode, Error.of(bindingResult));
    }

    public static ErrorResponse of(final ReturnCode returnCode) {
        return new ErrorResponse(returnCode);
    }

    public static ErrorResponse of(final ReturnCode returnCode, final List<Error> errors) {
        return new ErrorResponse(returnCode, errors);
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Error {

        String field;

        String value;

        String reason;

        private Error(final String field, final String value, final String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        public static List<Error> of(final String reason) {
            List<Error> errors = new ArrayList<>();
            if(StringUtils.hasText(reason)) errors.add(new Error(null, null, reason));
            return errors;
        }

        public static List<Error> of(BindingResult bindingResult) {
            final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new Error(
                            error.getField(),
                            ObjectUtils.isEmpty(error.getRejectedValue()) ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()))
                    .toList();
        }
    }
}
