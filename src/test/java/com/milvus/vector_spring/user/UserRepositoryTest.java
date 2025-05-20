package com.milvus.vector_spring.user;

import com.milvus.vector_spring.common.apipayload.status.ErrorStatus;
import com.milvus.vector_spring.common.exception.CustomException;
import com.milvus.vector_spring.config.QueryDslConfig;
import com.milvus.vector_spring.user.dto.UserSignUpRequestDto;
import com.milvus.vector_spring.user.dto.UserUpdateRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@Import(QueryDslConfig.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private UserSignUpRequestDto userSignUpRequestDto;
    private User user;

    @BeforeEach
    void setUp() {
        userSignUpRequestDto = new UserSignUpRequestDto(
                "hello",
                "hello@gmail.com",
                "asdf123!"
        );
        user = new User(1L, userSignUpRequestDto.getEmail(), userSignUpRequestDto.getUsername(), userSignUpRequestDto.getPassword(), null);
        userRepository.save(user);
    }

    @Test
    @DisplayName("유저 생성")
    void signUpUser() {
        userSignUpRequestDto = new UserSignUpRequestDto(
                "hello2",
                "hello2@gmail.com",
                "asdf123!"
        );
        user = User.builder()
                .email(userSignUpRequestDto.getEmail())
                .username(userSignUpRequestDto.getUsername())
                .password(userSignUpRequestDto.getPassword())
                .build();
        User createdUser = userRepository.save(user);
        User findSavedUser = userRepository.findById(createdUser.getId()).orElseThrow();
        assertThat(findSavedUser).isEqualTo(user);
    }

    @Test
    @DisplayName("유저 전체 조회")
    void findAllUsersTest() {
        for (int i = 1; i < 10; i++) {
            user = User.builder()
                    .email(String.format("hi%d@email.com", i))
                    .username(String.format("hi%d", i))
                    .password(userSignUpRequestDto.getPassword())
                    .build();
            userRepository.save(user);
        }
        List<User> userList = userRepository.findAll();
        assertThat(userList).hasSize(10);
        assertThat(userList.get(0).getEmail()).isEqualTo("hello@gmail.com");
    }

    @Test
    @DisplayName("유저 단일 조회")
    void findOneUserTestById() {
        User findUser = userRepository.findByEmail(userSignUpRequestDto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_FOUND_USER));

        assertThatThrownBy(() -> userRepository.findByEmail("notexist@email.com")
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_FOUND_USER)))
                .isInstanceOf(CustomException.class);
        assertThat(findUser).isNotNull();

    }

    @Test
    @DisplayName("유저 이름 수정")
    void updateUserName() {
        User user = userRepository.findByEmail(userSignUpRequestDto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_FOUND_USER));
        UserUpdateRequestDto userUpdateRequestDto = new UserUpdateRequestDto(
                "changeName",
                "change@email.com"
        );
        User updateUser = User.builder()
                .id(user.getId())
                .password(user.getPassword())
                .username(userUpdateRequestDto.getUsername())
                .email(userUpdateRequestDto.getEmail())
                .loginAt(user.getLoginAt())
                .build();
        User updatedUser = userRepository.save(updateUser);

        assertThat(user.getId()).isEqualTo(updatedUser.getId());
    }
}
