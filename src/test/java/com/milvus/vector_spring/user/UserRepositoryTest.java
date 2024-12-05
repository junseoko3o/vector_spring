package com.milvus.vector_spring.user;

import com.milvus.vector_spring.common.apipayload.status.ErrorStatus;
import com.milvus.vector_spring.common.exception.CustomException;
import com.milvus.vector_spring.config.QueryDslConfig;
import com.milvus.vector_spring.user.dto.UserSignUpRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@Import(QueryDslConfig.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    List<User> users = new ArrayList<>();

    @BeforeEach
    void setUp() throws CustomException {
        for (int i = 1; i < 10; i++) {
            UserSignUpRequestDto userSignUpRequestDto = new UserSignUpRequestDto(
                    String.format("hi%d", i),
                    String.format("hi%d@email.com", i),
                    "asdf123!"
            );

            User user = User.builder()
                    .email(userSignUpRequestDto.getEmail())
                    .username(userSignUpRequestDto.getUsername())
                    .password(userSignUpRequestDto.getPassword())
                    .build();

            users.add(userRepository.save(user));
        }
    }

    @Test
    @DisplayName("유저 생성")
    void signUpUser() {
        UserSignUpRequestDto userSignUpRequestDto = new UserSignUpRequestDto(
                "hello",
                "hello@gmail.com",
                "asdf123!"
        );

        User user = User.builder()
                .email(userSignUpRequestDto.getEmail())
                .username(userSignUpRequestDto.getUsername())
                .password(userSignUpRequestDto.getPassword())
                .build();

        User savedUser = userRepository.save(user);
        users.add(savedUser);
        User findSavedUser = userRepository.findById(10L).orElseThrow();
        assertThat(findSavedUser).isEqualTo(user);
    }

    @Test
    @DisplayName("유저 전체 조회")
    void findAllUserTest() {

        List<User> userList = userRepository.findAll();

        assertThat(userList).isNotEmpty();
        assertThat(userList.get(0).getUsername()).isEqualTo("hi1");
        assertThat(userList.size()).isEqualTo(9);
    }

    @Test
    @DisplayName("유저 단일 조회")
    void findOneUserTestById() {
        User user = userRepository.findByEmail("hi1@email.com")
                .orElseThrow(() -> new CustomException(ErrorStatus._NOT_FOUND_USER));

        assertThat(user).isEqualTo(users.get(0));
        assertThatThrownBy(() -> userRepository.findByEmail("notexist@email.com")
                .orElseThrow(() -> new CustomException(ErrorStatus._NOT_FOUND_USER)))
                .isInstanceOf(CustomException.class);

    }
}
