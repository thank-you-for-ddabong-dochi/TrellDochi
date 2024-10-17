package com.nbacm.trelldochi.domain.user.service;
import com.nbacm.trelldochi.domain.common.config.JwtUtil;
import com.nbacm.trelldochi.domain.common.config.PasswordEncoder;
import com.nbacm.trelldochi.domain.user.dto.UserRequestDto;
import com.nbacm.trelldochi.domain.user.dto.UserResponseDto;
import com.nbacm.trelldochi.domain.user.entity.User;
import com.nbacm.trelldochi.domain.user.entity.UserRole;
import com.nbacm.trelldochi.domain.user.exception.NotMatchPassword;
import com.nbacm.trelldochi.domain.user.exception.UserExistsException;
import com.nbacm.trelldochi.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void signUp_Success() {
        // given
        UserRequestDto requestDto = new UserRequestDto("test@example.com", "password", "testuser", UserRole.USER);
        when(userRepository.existsByEmail(requestDto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(requestDto.getPassword())).thenReturn("encodedPassword");

        // when
        UserResponseDto responseDto = userService.signUp(requestDto);

        // then
        assertEquals(requestDto.getEmail(), responseDto.getEmail());
        assertEquals(requestDto.getNickname(), responseDto.getNickname());  // 닉네임 검증
        verify(userRepository, times(1)).save(any(User.class));

        // 패스워드가 인코딩되었는지 확인
        verify(passwordEncoder, times(1)).encode(requestDto.getPassword());
    }

    @Test
    void signUp_ExistingEmailThrowsException() {
        // given
        UserRequestDto requestDto = new UserRequestDto("test@example.com", "password", "testuser", UserRole.USER);
        when(userRepository.existsByEmail(requestDto.getEmail())).thenReturn(true);

        // when & then
        assertThrows(UserExistsException.class, () -> userService.signUp(requestDto));
    }

    @Test
    void login_Success() {
        // given
        UserRequestDto requestDto = new UserRequestDto("test@example.com", "password", "testuser", UserRole.USER);
        User user = new User(requestDto.getEmail(), "encodedPassword", requestDto.getNickname(), UserRole.USER);

        when(userRepository.findByEmailOrElseThrow(requestDto.getEmail())).thenReturn(user);
        when(passwordEncoder.matches(requestDto.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtUtil.createToken(user.getEmail(), user.getUserRole())).thenReturn("jwtToken");

        // when
        String token = userService.login(requestDto);

        // then
        assertEquals("jwtToken", token);
    }

    @Test
    void login_InvalidPasswordThrowsException() {
        // given
        UserRequestDto requestDto = new UserRequestDto("test@example.com", "wrongpassword", "testuser", UserRole.USER);
        User user = new User(requestDto.getEmail(), "encodedPassword", requestDto.getNickname(), UserRole.USER);

        when(userRepository.findByEmailOrElseThrow(requestDto.getEmail())).thenReturn(user);
        when(passwordEncoder.matches(requestDto.getPassword(), user.getPassword())).thenReturn(false);

        // when & then
        assertThrows(NotMatchPassword.class, () -> userService.login(requestDto));
    }

    @Test
    void deleteUser_Success() {
        // given
        String email = "test@example.com";
        String password = "password";
        User user = new User(email, "encodedPassword", "testuser", UserRole.USER);

        when(userRepository.findByEmailOrElseThrow(email)).thenReturn(user);
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);

        // when
        userService.deleteUser(email, password);

        // then
        verify(userRepository, times(1)).save(user);
        assertTrue(user.getIsDelete());
    }

    @Test
    void deleteUser_WrongPasswordThrowsException() {
        // given
        String email = "test@example.com";
        String password = "wrongpassword";
        User user = new User(email, "encodedPassword", "testuser", UserRole.USER);

        when(userRepository.findByEmailOrElseThrow(email)).thenReturn(user);
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(false);

        // when & then
        assertThrows(NotMatchPassword.class, () -> userService.deleteUser(email, password));
    }
}
