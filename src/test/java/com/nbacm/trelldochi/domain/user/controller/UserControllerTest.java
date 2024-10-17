package com.nbacm.trelldochi.domain.user.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbacm.trelldochi.domain.common.config.JwtUtil;
import com.nbacm.trelldochi.domain.common.dto.CustomUserDetails;
import com.nbacm.trelldochi.domain.common.service.CustomUserDetailsService;
import com.nbacm.trelldochi.domain.user.dto.UserRequestDto;
import com.nbacm.trelldochi.domain.user.dto.UserResponseDto;
import com.nbacm.trelldochi.domain.user.entity.User;
import com.nbacm.trelldochi.domain.user.entity.UserRole;
import com.nbacm.trelldochi.domain.user.service.UserServiceImpl;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private CustomUserDetailsService userDetailsService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        User user = new User("test@example.com", "encodedPassword", "testuser", UserRole.USER);
        CustomUserDetails userDetails = new CustomUserDetails(user);
        when(userDetailsService.loadUserByUsername("test@example.com")).thenReturn(userDetails);
    }

    @Test
    void signUp_Success() throws Exception {
        // given
        UserRequestDto requestDto = new UserRequestDto("test@example.com", "password", "testuser", UserRole.USER);
        UserResponseDto responseDto = new UserResponseDto("test@example.com", "testuser", UserRole.USER);

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
