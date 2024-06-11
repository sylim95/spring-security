package com.task.config.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtUtils {

    // JWT SECRET KEY
    @Value("${security.jwt.secret-key}")
    private String TOKEN_SECRET_KEY;
    private static SecretKey key;

    @Value("${security.jwt.accessTokenExpiration}")
    private long ACCESS_TOKEN_EXPIRATION;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(TOKEN_SECRET_KEY));
    }

    /**
     * 토큰 생성
     * */
    public String generateToken(UserDetails userDetails) {
        ClaimsBuilder claims = Jwts.claims();
        claims.add("username", userDetails.getUsername());
        return createToken(claims.build(), userDetails.getUsername());
    }

    private String createToken(Claims claims, String subjecct) {
        return Jwts.builder()
                .subject(subjecct)
                .claims()
                .add(claims)
                .and()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 토큰 Claims 디코딩
     * */
    public static Claims parseToken(String jwt) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }

    /**
     * 토큰 유효성 여부
     */
    public boolean isValid(String jwt) {
        return getStatus(jwt) == JwtVerifyResult.TOKEN_VALID;
    }

    /**
     * 토큰 상태 체크
     * ExpiredJwtException: 유효 만료
     * SignatureException: invalid
     * */
    public JwtVerifyResult getStatus(String jwt) {
        if (StringUtils.isBlank(jwt)) {
            return JwtVerifyResult.TOKEN_NOT_FOUND;
        }

        try {
            Claims claims = parseToken(jwt);
            log.debug("JWT Expiration = {}", claims.getExpiration());
            return JwtVerifyResult.TOKEN_VALID;
        } catch (ExpiredJwtException e) {
            log.error(JwtVerifyResult.TOKEN_EXPIRED.getErrorMessage(), e);
            return JwtVerifyResult.TOKEN_EXPIRED;
        } catch (Exception e) {
            log.error(JwtVerifyResult.TOKEN_INVALID.getErrorMessage(), e);
            return JwtVerifyResult.TOKEN_INVALID;
        }
    }

    /**
     * token 에서 username 가져오기
     * */
    public String getUserName(String token) {
        String username = String.valueOf(parseToken(token).get("username"));
        log.info("token username subject: {}", username);
        return username;
    }

}
