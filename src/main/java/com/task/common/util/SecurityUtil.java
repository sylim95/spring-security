package com.task.common.util;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtil {

    /**
     * 현재 로그인 아이디 구하기
     * */
    public static String getCurrentLoginUserId() {
        SecurityContext securityContext = SecurityContextHolder.getContext();

        if (securityContext.getAuthentication() == null) {
            return null;
        }

        Authentication authentication = securityContext.getAuthentication();

        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            if(userDetails == null) return null;
            return userDetails.getUsername();
        } else {
            return null;
        }
    }
}
