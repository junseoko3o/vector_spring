package com.milvus.vector_spring.user;

import com.milvus.vector_spring.common.exception.CustomException;
import com.milvus.vector_spring.user.dto.UserProjectsResponseDto;
import com.milvus.vector_spring.user.dto.UserResponseDto;
import com.milvus.vector_spring.user.dto.UserSignUpRequestDto;
import com.milvus.vector_spring.user.dto.UserUpdateRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity<List<User>> findAllUser() {
        List<User> users = userService.findAllUser();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserResponseDto> signUpUser(@Validated @RequestBody UserSignUpRequestDto userSignUpRequestDto) throws CustomException {
        User user = userService.signUpUser(userSignUpRequestDto);
        UserResponseDto response = UserResponseDto.of(user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable() Long id, @Validated @RequestBody UserUpdateRequestDto userUpdateRequestDto) throws CustomException {
        User user = userService.updateUser(id, userUpdateRequestDto);
        UserResponseDto response = UserResponseDto.of(user);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/project/{id}")
    public ResponseEntity<UserProjectsResponseDto> getUser(@PathVariable("id") Long id) throws CustomException {
        UserProjectsResponseDto user = userService.fineOneUserWithProjects(id);
        return ResponseEntity.ok(user);
    }
}
