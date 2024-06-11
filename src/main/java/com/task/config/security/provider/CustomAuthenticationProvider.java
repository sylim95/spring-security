package com.task.config.security.provider;

import com.task.auth.service.UserDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(@Qualifier("userDetailServiceImpl") UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) authentication;

        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationToken.getName());

        if (!userDetails.isEnabled()) {
            log.warn("사용할 수 없는 계정 :: userDetail={}", ToStringBuilder.reflectionToString(userDetails, ToStringStyle.MULTI_LINE_STYLE));
            throw new DisabledException("사용할 수 없는 계정입니다. :: userId=" + authenticationToken.getName());
        }

        if (StringUtils.isBlank(userDetails.getPassword())) {
            log.warn("비정상 상태의 계정 (password = null) :: userDetail={}", ToStringBuilder.reflectionToString(userDetails, ToStringStyle.MULTI_LINE_STYLE));
            throw new BadCredentialsException("사용할 수 없는 계정입니다. :: userId=" + authenticationToken.getName());
        }

        String presentedPassword = authentication.getCredentials().toString();
        if (!passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
            log.warn("비밀번호 불일치 :: userId={}", authenticationToken.getName());
            throw new BadCredentialsException("비밀번호가 일치 하지 않습니다. :: userId=" + authenticationToken.getName());
        }

        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
