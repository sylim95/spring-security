package com.task.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.task.common.code.ReturnCode;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {

    private Integer code;

    private String message;

    T data;

    public static <T> BaseResponse<T> success() {
        return new BaseResponse<>(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), null);
    }

    public static <T> BaseResponse<T> success(final T resData) {
        return new BaseResponse<>(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), resData);
    }
}
