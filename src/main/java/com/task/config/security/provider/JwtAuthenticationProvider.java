package com.task.config.security.provider;

import com.task.auth.service.UserDetailServiceImpl;
import com.task.config.security.exception.JwtInvalidException;
import com.task.config.security.jwt.JwtUtils;
import com.task.config.security.token.JwtAuthenticationToken;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final UserDetailServiceImpl userDetailService;
    private final JwtUtils jwtUtils;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = ((JwtAuthenticationToken) authentication).getAccessToken();
        String username = "";

        log.info("JwtAuthenticationFilter > token: {}", token);

        if(jwtUtils.isValid(token)) {
            username = jwtUtils.getUserName(token);
        }

        if(StringUtils.isEmpty(username)) {
            throw new JwtInvalidException(String.format("올바르지 않은 토큰입니다. :: token=%s", token));
        }

        UserDetails userDetails = userDetailService.loadUserByUsername(username);
        log.debug("계정 정보 : {}", userDetails);

        if (ObjectUtils.isEmpty(userDetails)) {
            throw new UsernameNotFoundException(String.format("회원정보를 찾을 수 없습니다. :: username=%s", username));
        }

        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        jwtAuthenticationToken.setToken(token);

        return jwtAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(JwtAuthenticationToken.class);
    }
}
