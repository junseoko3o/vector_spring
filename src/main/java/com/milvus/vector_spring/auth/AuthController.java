package com.milvus.vector_spring.auth;

import com.milvus.vector_spring.auth.dto.UserLoginCheckResponseDto;
import com.milvus.vector_spring.auth.dto.UserLoginRequestDto;
import com.milvus.vector_spring.auth.dto.UserLoginResponseDto;
import com.milvus.vector_spring.common.annotation.RequireToken;
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
//        String token = userLoginResponseDto.getAccessToken();
//        CookieUtil.addCookie(httpServletResponse, "accessToken", token);
        return ResponseEntity.ok(userLoginResponseDto);
    }

    @GetMapping("/check")
    @RequireToken
    public ResponseEntity<UserLoginCheckResponseDto> loginCheck() {
        return ResponseEntity.ok(authService.loginCheck());
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout() {
//        String token = CookieUtil.getCookie(request, "accessToken");
        authService.logout();
//        CookieUtil.deleteCookie(response, "accessToken" );
        return ResponseEntity.ok("로그아웃 완료!");
    }
}