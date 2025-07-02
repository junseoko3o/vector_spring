package com.milvus.vector_spring.user;

import com.milvus.vector_spring.common.annotation.NoAuthRequired;
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
    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;

    public UserController(UserQueryService userQueryService, UserCommandService userCommandService) {
        this.userQueryService = userQueryService;
        this.userCommandService = userCommandService;
    }


    @GetMapping()
    public List<UserResponseDto> findAllUser() {
        List<User> users = userQueryService.findAllUser();
        return users.stream()
                .map(UserResponseDto::userResponseDto)
                .toList();
    }

    @PostMapping("/sign-up")
    @NoAuthRequired
    public ResponseEntity<UserResponseDto> signUpUser(@Validated @RequestBody UserSignUpRequestDto userSignUpRequestDto) throws CustomException {
        User user = userCommandService.signUpUser(userSignUpRequestDto);
        return ResponseEntity.ok(UserResponseDto.userResponseDto(user));
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable() Long id, @Validated @RequestBody UserUpdateRequestDto userUpdateRequestDto) throws CustomException {
        User user = userCommandService.updateUser(id, userUpdateRequestDto);
        return ResponseEntity.ok(UserResponseDto.userResponseDto(user));
    }

    @GetMapping("/project/{id}")
    public UserProjectsResponseDto getUser(@PathVariable("id") Long id) throws CustomException {
        return userQueryService.findOneUserWithProjects(id);
    }
}
