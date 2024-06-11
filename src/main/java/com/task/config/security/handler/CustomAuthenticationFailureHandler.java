package com.task.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.common.dto.BaseResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String resultMsg = "인증 오류가 발생하였습니다.";
        if (exception instanceof BadCredentialsException) {
            log.warn("로그인 정보 입력이 잘못되었거나 계정이 올바르지 않습니다. :: {}", exception.getMessage());
            resultMsg = "로그인 정보 입력이 잘못되었거나 계정이 올바르지 않습니다.";
        } else if (exception instanceof AccountStatusException) {
            resultMsg = "계정 상태가 올바르지 않습니다.";
            log.warn("계정 상태가 올바르지 않습니다. :: {}", exception.getMessage());
        } else {
            log.warn(resultMsg, exception);
        }

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        try {
            response.getWriter().write(
                    objectMapper.writeValueAsString(
                            BaseResponse.builder()
                                    .code(HttpStatus.UNAUTHORIZED.value())
                                    .message(resultMsg)
                                    .build()
                    )
            );
        } catch (Exception e) {
            log.warn("응답 Body 작성중 오류가 발생하였습니다.", e);
        }
    }
}
