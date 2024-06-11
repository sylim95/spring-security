package com.task.config.security.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.auth.dto.request.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;

/**
 * 로그인 필터
 * */
@Slf4j
public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    public LoginFilter(String filterProcessUrl, AuthenticationManager authenticationManager) {
        super(filterProcessUrl);
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST") || !request.getContentType().equals("application/json")) {
            throw new AuthenticationServiceException("login method not supported: " + request.getMethod());
        }

        LoginRequest loginRequest;
        try {
            loginRequest = new ObjectMapper().readValue(request.getReader(), LoginRequest.class);
        } catch (IOException e) {
            log.warn("Json parsing error: ", e);
            throw new IllegalArgumentException("로그인 요청 형식이 올바르지 않습니다.");
        }

        UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getUserId(), loginRequest.getPassword()
        );

        return getAuthenticationManager().authenticate(userToken);
    }
}
