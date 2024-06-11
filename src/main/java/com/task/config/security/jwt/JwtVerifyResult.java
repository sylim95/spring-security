package com.task.config.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum JwtVerifyResult {
    TOKEN_VALID(HttpStatus.OK, "인증 토큰이 확인되었습니다."),
    TOKEN_CREATE_REQ(HttpStatus.OK, "인증 토큰생성 요청."),
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "인증 토큰을 확인 할 수 없습니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "TOKEN이 만료 되었습니다."),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "TOKEN이 유효하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String errorMessage;
}
