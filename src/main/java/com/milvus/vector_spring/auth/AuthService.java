package com.milvus.vector_spring.auth;

import com.milvus.vector_spring.auth.dto.UserLoginRequestDto;
import com.milvus.vector_spring.auth.dto.UserLoginResponseDto;
import com.milvus.vector_spring.common.apipayload.status.ErrorStatus;
import com.milvus.vector_spring.common.exception.CustomException;
import com.milvus.vector_spring.common.service.RedisService;
import com.milvus.vector_spring.config.jwt.JwtTokenProvider;
import com.milvus.vector_spring.user.User;
import com.milvus.vector_spring.user.UserDetailService;
import com.milvus.vector_spring.user.UserRepository;
import com.milvus.vector_spring.user.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserDetailService userDetailService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RedisService redisService;
    private final UserService userService;
    private final HttpServletRequest request;

    public UserLoginResponseDto login(UserLoginRequestDto userLoginRequestDto) {
        User user = userDetailService.loadUserByUsername(userLoginRequestDto.getEmail());
        if (!bCryptPasswordEncoder.matches(userLoginRequestDto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorStatus._NOT_PASSWORD_MATCHES);
        };
        user.updateLoginAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);

        String accessToken = jwtTokenProvider.generateAccessToken(user);
        jwtTokenProvider.generateRefreshToken(user);
        return new UserLoginResponseDto(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getUsername(),
                accessToken,
                savedUser.getLoginAt()
        );
    }

    public void logout() {
        String token = getToken();
        if (jwtTokenProvider.validateToken(token)) {
            Claims claims = jwtTokenProvider.getClaims(token);
            Long userId = claims.get("userId", Long.class);
            User user = userService.findOneUser(userId);

            String redisKey = "refreshToken:" + user.getEmail();
            redisService.deleteRedis(redisKey);
        } else {
            throw new CustomException(ErrorStatus._INVALID_ACCESS_TOKEN);
        }
    }

    public UserLoginResponseDto check() {
        String token = getToken();
        Claims claims = jwtTokenProvider.getClaims(token);
        User user = userService.findOneUser(claims.get("userId", Long.class));
        return new UserLoginResponseDto(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                token,
                user.getLoginAt()
        );
    }

    private String getToken() {
        String token = request.getHeader("Authorization");
        return token.replace("Bearer ", "");
    }
}
