package com.milvus.vector_spring.auth;

import com.milvus.vector_spring.auth.dto.UserLoginCheckResponseDto;
import com.milvus.vector_spring.auth.dto.UserLoginRequestDto;
import com.milvus.vector_spring.auth.dto.UserLoginResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(@Validated @RequestBody UserLoginRequestDto userLoginRequestDto) {
        UserLoginResponseDto userLoginResponseDto = authService.login(userLoginRequestDto);
        return ResponseEntity.ok(userLoginResponseDto);
    }

    @GetMapping("/check")
    public ResponseEntity<UserLoginCheckResponseDto> loginCheck(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(authService.loginCheck(token));
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token);
        return ResponseEntity.ok("로그아웃 완료!");
    }
}