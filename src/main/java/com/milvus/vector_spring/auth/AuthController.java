package com.milvus.vector_spring.auth;

import com.milvus.vector_spring.auth.dto.UserLoginCheckResponseDto;
import com.milvus.vector_spring.auth.dto.UserLoginRequestDto;
import com.milvus.vector_spring.auth.dto.UserLoginResponseDto;
import com.milvus.vector_spring.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
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

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(@Validated @RequestBody UserLoginRequestDto userLoginRequestDto) {
        UserLoginResponseDto userLoginResponseDto = authService.login(userLoginRequestDto);
//        String token = userLoginResponseDto.getAccessToken();
//        CookieUtil.addCookie(httpServletResponse, "accessToken", token, 7 * 24 * 60 * 60);
        return ResponseEntity.ok(userLoginResponseDto);
    }

    @GetMapping("/check")
    public ResponseEntity<UserLoginCheckResponseDto> loginCheck(@RequestHeader("Authorization") String token) {
//        String token = CookieUtil.getCookie(request, "accessToken");
        return ResponseEntity.ok(authService.loginCheck(token));
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
//        String token = CookieUtil.getCookie(request, "accessToken");
        authService.logout(token);
//        CookieUtil.deleteCookie(response, "accessToken" );
        return ResponseEntity.ok("로그아웃 완료!");
    }
}