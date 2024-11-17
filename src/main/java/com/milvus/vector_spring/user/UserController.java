package com.milvus.vector_spring.user;

import com.milvus.vector_spring.exception.CustomException;
import com.milvus.vector_spring.user.dto.UserSignUpRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<User> signUpUser(UserSignUpRequestDto userSignUpRequestDto) throws CustomException {
        User user = userService.signUpUser(userSignUpRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable() Long id, @RequestBody UserSignUpRequestDto userSignUpRequestDto) throws CustomException {
        User user = userService.updateUser(id, userSignUpRequestDto);
        return ResponseEntity.ok(user);
    }
}
