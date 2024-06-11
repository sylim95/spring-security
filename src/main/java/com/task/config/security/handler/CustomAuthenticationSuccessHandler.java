package com.task.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.auth.dto.response.LoginResponse;
import com.task.common.code.ReturnCode;
import com.task.config.security.jwt.JwtUtils;
import com.task.error.exception.BusinessException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * packageName    : com.task.config.security.handler
 * fileName       : CustomAuthenticationSuccessHandler
 * author         : limsooyoung
 * date           : 5/30/24
 * description    : 로그인 성공 시 토큰 발행
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 5/30/24        limsooyoung       최초 생성
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // 토큰 발행
            String token = jwtUtils.generateToken(userDetails);

            makeResponse(response, HttpStatus.OK.value(), LoginResponse.builder().accessToken(token).build());
        } catch(Exception e) {
            log.error("토큰 발행 실패 : ", e);
            makeResponse(response, HttpStatus.UNAUTHORIZED.value(), BusinessException.from(ReturnCode.ERROR_401));
        }
    }

    private void makeResponse(HttpServletResponse response, int status, Object responseData) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(status);
        response.getWriter().write(objectMapper.writeValueAsString(responseData));
    }
}
