package com.milvus.vector_spring.user;

import com.milvus.vector_spring.user.dto.UserSignUpRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public List<User> findAllUser() {
        return userService.findAllUser();
    }

    @PostMapping("/sign-up")
    public ResponseEntity<User> signUpUser(UserSignUpRequestDto userSignUpRequestDto) {
        User user = userService.signUpUser(userSignUpRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
