package com.milvus.vector_spring.auth;

import com.milvus.vector_spring.auth.dto.UserLoginRequestDto;
import com.milvus.vector_spring.auth.dto.UserLoginResponseDto;
import com.milvus.vector_spring.util.CookieUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {
    private final AuthService authService;
    private final CookieUtil cookieUtil;

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(@Validated @RequestBody UserLoginRequestDto userLoginRequestDto, HttpServletResponse httpServletResponse) {
        UserLoginResponseDto userLoginResponseDto = authService.login(userLoginRequestDto);
        CookieUtil.addCookie(httpServletResponse, "Authorization", userLoginResponseDto.getAccessToken(), 900000);
        return ResponseEntity.ok(userLoginResponseDto);
    }

    @GetMapping("/check")
    public ResponseEntity<Claims> loginCheck(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(authService.loginCheck(token));
    }
}