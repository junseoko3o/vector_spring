package com.milvus.vector_spring.auth;

import com.milvus.vector_spring.auth.dto.UserLoginRequestDto;
import com.milvus.vector_spring.auth.dto.UserLoginResponseDto;
import com.milvus.vector_spring.common.RedisService;
import com.milvus.vector_spring.common.apipayload.status.ErrorStatus;
import com.milvus.vector_spring.common.exception.CustomException;
import com.milvus.vector_spring.config.jwt.JwtTokenProvider;
import com.milvus.vector_spring.user.User;
import com.milvus.vector_spring.user.UserDetailService;
import com.milvus.vector_spring.user.UserRepository;
import io.jsonwebtoken.Claims;
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

    public UserLoginResponseDto login(UserLoginRequestDto userLoginRequestDto) {
        User user = userDetailService.loadUserByUsername(userLoginRequestDto.getEmail());
        if (!bCryptPasswordEncoder.matches(userLoginRequestDto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorStatus._NOT_PASSWORD_MATCHES);
        };
        user.updateLoginAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);

        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);
        return new UserLoginResponseDto(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getUsername(),
                accessToken,
                refreshToken,
                savedUser.getLoginAt()
        );
    }

    public Claims loginCheck(String token) {
        String accessToken = token.replace("Bearer ", "");
        Claims user = jwtTokenProvider.getClaims(accessToken);
        String email = user.get("email").toString();
        String refreshToken = redisService.getRedis("refreshToken:" + email);
        if (refreshToken != null && !refreshToken.isEmpty()) {
            return user;
        } else {
            throw new CustomException(ErrorStatus._EMPTY_REFRESH_TOKEN);
        }
    }
}
