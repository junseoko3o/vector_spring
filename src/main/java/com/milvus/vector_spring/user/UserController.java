package com.milvus.vector_spring.user;

import com.milvus.vector_spring.common.exception.CustomException;
import com.milvus.vector_spring.user.dto.UserProjectsResponseDto;
import com.milvus.vector_spring.user.dto.UserResponseDto;
import com.milvus.vector_spring.user.dto.UserSignUpRequestDto;
import com.milvus.vector_spring.user.dto.UserUpdateRequestDto;
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
    public List<UserResponseDto> findAllUser() {
        List<User> users = userService.findAllUser();
        return users.stream()
                .map(UserResponseDto::of)
                .toList();
    }

    @PostMapping("/sign-up")
    public UserResponseDto signUpUser(@Validated @RequestBody UserSignUpRequestDto userSignUpRequestDto) throws CustomException {
        User user = userService.signUpUser(userSignUpRequestDto);
        return UserResponseDto.of(user);
    }

    @PostMapping("/update/{id}")
    public UserResponseDto updateUser(@PathVariable() Long id, @Validated @RequestBody UserUpdateRequestDto userUpdateRequestDto) throws CustomException {
        User user = userService.updateUser(id, userUpdateRequestDto);
        return UserResponseDto.of(user);
    }

    @GetMapping("/project/{id}")
    public UserProjectsResponseDto getUser(@PathVariable("id") Long id) throws CustomException {
        return userService.fineOneUserWithProjects(id);
    }
}
