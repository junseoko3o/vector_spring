package com.milvus.vector_spring.config.jwt;

import com.milvus.vector_spring.common.RedisService;
import com.milvus.vector_spring.user.User;
import com.milvus.vector_spring.user.UserDetailService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.token.secret.key}")
    private String secretKey;

    @Value("${jwt.access.token.expiration}")
    private int accessTokenExpiration;

    @Value("${jwt.refresh.token.expiration}")
    private int refreshTokenExpiration;

    private final RedisService redisService;
    private final UserDetailService userDetailService;

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(User user) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryDate = now.plusSeconds(accessTokenExpiration / 1000);
        Header jwtHeader = Jwts.header()
                .type("JWT")
                .build();
        return Jwts.builder()
                .header().add(jwtHeader)
                .and()
                .subject(user.getEmail())
                .issuedAt(java.sql.Timestamp.valueOf(now))
                .expiration(java.sql.Timestamp.valueOf(expiryDate))
                .claims(userToMap(user))
                .signWith(this.getSigningKey())
                .compact();
    }

    public String generateRefreshToken(User user) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryDate = now.plusSeconds(refreshTokenExpiration / 1000);

        String refreshToken = Jwts.builder()
                .issuedAt(java.sql.Timestamp.valueOf(now))
                .expiration(java.sql.Timestamp.valueOf(expiryDate))
                .signWith(this.getSigningKey())
                .compact();

        redisService.setRedis(
                "refreshToken:" + user.getEmail(),
                refreshToken,
                refreshTokenExpiration / 1000
        );

        return refreshToken;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(this.getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);

        UserDetails userDetails = userDetailService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(this.getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    public void deleteRefreshToken(String email) {
        redisService.deleteRedis("refreshToken:" + email);
    }

    private Map<String, Object> userToMap(User user) {
        Map<String, Object> userMap = new HashMap<>();

        userMap.put("userId", user.getId());
        userMap.put("email", user.getEmail());
        userMap.put("userName", user.getUsername());

        return userMap;
    }
}
