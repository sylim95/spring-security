package com.task.config.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.auth.dto.request.LoginRequest;
import com.task.config.security.filter.LoginFilter;
import com.task.config.security.provider.CustomAuthenticationProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoginFilterTest {
    private LoginFilter loginFilter;
    private CustomAuthenticationProvider customAuthenticationProvider;
    private AuthenticationManager authenticationManager;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ObjectMapper objectMapper;
    private UserDetailsService userDetailsService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        authenticationManager = mock(AuthenticationManager.class);
        loginFilter = new LoginFilter("/login", authenticationManager);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        objectMapper = new ObjectMapper();

        userDetailsService = mock(UserDetailsService.class);
        passwordEncoder = mock(PasswordEncoder.class);
        customAuthenticationProvider = new CustomAuthenticationProvider(userDetailsService, passwordEncoder);
    }

    @DisplayName("로그인 성공")
    @Test
    void login() throws IOException {
        when(request.getMethod()).thenReturn("POST");
        when(request.getContentType()).thenReturn("application/json");

        LoginRequest loginRequest = new LoginRequest("test", "1234");
        String jsonRequest = objectMapper.writeValueAsString(loginRequest);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonRequest)));

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken("test", "1234");

        UserDetails userDetails = new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public String getPassword() {
                return "1234";
            }

            @Override
            public String getUsername() {
                return "test";
            }

            @Override
            public boolean isAccountNonExpired() {
                return false;
            }

            @Override
            public boolean isAccountNonLocked() {
                return false;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return false;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };

        when(userDetailsService.loadUserByUsername("test")).thenReturn(userDetails);
        when(passwordEncoder.matches("1234", userDetails.getPassword())).thenReturn(true);

        when(authenticationManager.authenticate(authRequest)).thenAnswer(invocation -> customAuthenticationProvider.authenticate(authRequest));

        Authentication result = loginFilter.attemptAuthentication(request, response);

        assertNotNull(result);
        assertEquals("test", result.getName());
    }

    @DisplayName("로그인 실패 :: 메소드 형식이 올바르지 않음")
    @Test
    void loginInvalidMethod() {
        when(request.getMethod()).thenReturn("GET");

        AuthenticationException exception = assertThrows(AuthenticationServiceException.class, () -> {
            loginFilter.attemptAuthentication(request, response);
        });

        assertEquals("login method not supported: GET", exception.getMessage());
    }

    @DisplayName("로그인 실패 :: content type이 올바르지 않음")
    @Test
    void loginInvalidContentType() {
        when(request.getMethod()).thenReturn("POST");
        when(request.getContentType()).thenReturn("text/plain");

        AuthenticationException exception = assertThrows(AuthenticationServiceException.class, () -> {
            loginFilter.attemptAuthentication(request, response);
        });

        assertEquals("login method not supported: POST", exception.getMessage());
    }

    @DisplayName("로그인 실패 :: 사용할 수 없는 계정 :: unable")
    @Test
    void loginUnAble() throws IOException {
        when(request.getMethod()).thenReturn("POST");
        when(request.getContentType()).thenReturn("application/json");

        LoginRequest loginRequest = new LoginRequest("test", "1234");
        String jsonRequest = objectMapper.writeValueAsString(loginRequest);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonRequest)));

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken("test", "1234");

        UserDetails userDetails = new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public String getPassword() {
                return "1234";
            }

            @Override
            public String getUsername() {
                return "test";
            }

            @Override
            public boolean isAccountNonExpired() {
                return false;
            }

            @Override
            public boolean isAccountNonLocked() {
                return false;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return false;
            }

            @Override
            public boolean isEnabled() {
                return false;
            }
        };

        when(userDetailsService.loadUserByUsername("test")).thenReturn(userDetails);
        when(passwordEncoder.matches("1234", userDetails.getPassword())).thenReturn(true);

        when(authenticationManager.authenticate(authRequest)).thenAnswer(invocation -> customAuthenticationProvider.authenticate(authRequest));

        DisabledException exception = assertThrows(DisabledException.class, () -> {
            loginFilter.attemptAuthentication(request, response);
        });

        assertEquals("사용할 수 없는 계정입니다. :: userId=" + authRequest.getName(), exception.getMessage());
    }

    @DisplayName("로그인 실패 :: 사용할 수 없는 계정 :: 비정상 계정")
    @Test
    void loginUserNoPassword() throws IOException {
        when(request.getMethod()).thenReturn("POST");
        when(request.getContentType()).thenReturn("application/json");

        LoginRequest loginRequest = new LoginRequest("test", "1234");
        String jsonRequest = objectMapper.writeValueAsString(loginRequest);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonRequest)));

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken("test", "1234");

        UserDetails userDetails = new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public String getPassword() {
                return "";
            }

            @Override
            public String getUsername() {
                return "test";
            }

            @Override
            public boolean isAccountNonExpired() {
                return false;
            }

            @Override
            public boolean isAccountNonLocked() {
                return false;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return false;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };

        when(userDetailsService.loadUserByUsername("test")).thenReturn(userDetails);
        when(passwordEncoder.matches("1234", userDetails.getPassword())).thenReturn(true);

        when(authenticationManager.authenticate(authRequest)).thenAnswer(invocation -> customAuthenticationProvider.authenticate(authRequest));

        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> {
            loginFilter.attemptAuthentication(request, response);
        });

        assertEquals("사용할 수 없는 계정입니다. :: userId=" + authRequest.getName(), exception.getMessage());
    }

    @DisplayName("로그인 실패 :: 비밀번호 불일치")
    @Test
    void loginPasswordNotMatch() throws IOException {
        when(request.getMethod()).thenReturn("POST");
        when(request.getContentType()).thenReturn("application/json");

        LoginRequest loginRequest = new LoginRequest("test", "1234");
        String jsonRequest = objectMapper.writeValueAsString(loginRequest);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonRequest)));

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken("test", "1234");

        UserDetails userDetails = new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public String getPassword() {
                return "5678";
            }

            @Override
            public String getUsername() {
                return "test";
            }

            @Override
            public boolean isAccountNonExpired() {
                return false;
            }

            @Override
            public boolean isAccountNonLocked() {
                return false;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return false;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };

        when(userDetailsService.loadUserByUsername("test")).thenReturn(userDetails);
        when(passwordEncoder.matches("1234", userDetails.getPassword())).thenReturn(false);

        when(authenticationManager.authenticate(authRequest)).thenAnswer(invocation -> customAuthenticationProvider.authenticate(authRequest));

        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> {
            loginFilter.attemptAuthentication(request, response);
        });

        assertEquals("비밀번호가 일치 하지 않습니다. :: userId=" + authRequest.getName(), exception.getMessage());
    }
}
