package com.task.error.exception;

import com.task.common.code.ReturnCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BusinessException extends RuntimeException {

    private ReturnCode returnCode;

    public BusinessException(String message, ReturnCode returnCode) {
        super(message);
        this.returnCode = returnCode;
    }

    public static BusinessException from(ReturnCode returnCode) {
        return new BusinessException(returnCode);
    }

    public static BusinessException of(String message, ReturnCode returnCode) {
        return new BusinessException(message, returnCode);
    }
}
