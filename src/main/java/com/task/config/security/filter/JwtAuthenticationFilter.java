package com.task.config.security.filter;

import com.task.config.security.handler.CustomAuthenticationFailureHandler;
import com.task.config.security.token.JwtAuthenticationToken;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 토큰 검증을 위한 필터
 * */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authManager;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    private final List<String> excludeUrlPatterns = List.of("/login", "/signup");

    public JwtAuthenticationFilter(AuthenticationManager authManager, CustomAuthenticationFailureHandler customAuthenticationFailureHandler) {
        this.authManager = authManager;
        this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        return excludeUrlPatterns.stream()
                .anyMatch(p -> pathMatcher.match(p, request.getServletPath()));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        if (StringUtils.isNotEmpty(authorization) && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);

            try {
                Authentication jwtAuthenticationToken = new JwtAuthenticationToken(token); // 인증 받기 전 토큰
                Authentication authentication = authManager.authenticate(jwtAuthenticationToken); // 인증 실시

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (AuthenticationException e) {
                log.warn("인증 실패", e);
                SecurityContextHolder.clearContext();
                customAuthenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        } else {
            log.warn("인증 실패 - 토큰을 찾을 수 없습니다.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            customAuthenticationFailureHandler.onAuthenticationFailure(request, response, null);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
