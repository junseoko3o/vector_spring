package com.milvus.vector_spring.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.milvus.vector_spring.user.dto.UserSignUpRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원 가입 성공")
    @WithMockUser
    void ifUserSignUpComplete() throws Exception {
        UserSignUpRequestDto userSignUpRequestDto = new UserSignUpRequestDto(
                "test1",
                "test1@email.com",
                "asdf123!"
        );

        User mockUser = User.builder()
                .id(1L)
                .email(userSignUpRequestDto.getEmail())
                .username(userSignUpRequestDto.getUsername())
                .password(userSignUpRequestDto.getPassword())
                .build();

        when(userService.signUpUser(any(UserSignUpRequestDto.class))).thenReturn(mockUser);

        mockMvc.perform(post("/user/sign-up")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(userSignUpRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test1@email.com"));

        verify(userService, times(1)).signUpUser(any(UserSignUpRequestDto.class));
    }
}
