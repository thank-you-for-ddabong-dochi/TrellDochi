package com.nbacm.trelldochi.domain.user.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbacm.trelldochi.domain.user.dto.UserRequestDto;
import com.nbacm.trelldochi.domain.user.dto.UserResponseDto;
import com.nbacm.trelldochi.domain.user.entity.UserRole;
import com.nbacm.trelldochi.domain.user.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void signUp_Success() throws Exception {
        // given
        UserRequestDto requestDto = new UserRequestDto("test@example.com", "password", "testuser", UserRole.USER);
        UserResponseDto responseDto = new UserResponseDto("test@example.com", "testuser", UserRole.USER);

        // Mocking the service layer
        when(userService.signUp(any(UserRequestDto.class))).thenReturn(responseDto);

        // when & then
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("회원가입 성공"))
                .andExpect(jsonPath("$.data.email").value("test@example.com"))
                .andExpect(jsonPath("$.data.nickname").value("testuser"));
    }

    @Test
    void login_Success() throws Exception {
        // given
        UserRequestDto requestDto = new UserRequestDto("test@example.com", "password", "testuser", UserRole.USER);
        String token = "jwtToken";

        // Mocking the service layer
        when(userService.login(any(UserRequestDto.class))).thenReturn(token);

        // when & then
        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("로그인 성공"))
                .andExpect(jsonPath("$.data").value(token));
    }
}