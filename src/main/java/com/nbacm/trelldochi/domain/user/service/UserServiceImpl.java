package com.nbacm.trelldochi.domain.user.service;

import com.nbacm.trelldochi.domain.common.config.JwtUtil;
import com.nbacm.trelldochi.domain.common.config.PasswordEncoder;
import com.nbacm.trelldochi.domain.notifications.service.NotificationService;
import com.nbacm.trelldochi.domain.user.dto.UserRequestDto;
import com.nbacm.trelldochi.domain.user.dto.UserResponseDto;
import com.nbacm.trelldochi.domain.user.entity.User;
import com.nbacm.trelldochi.domain.user.entity.UserRole;
import com.nbacm.trelldochi.domain.user.exception.NotMatchPassword;
import com.nbacm.trelldochi.domain.user.exception.UserExistsException;
import com.nbacm.trelldochi.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final NotificationService notificationService;


    @Transactional
    @Override
    public UserResponseDto signUp(UserRequestDto userRequestDto) {
        if(userRepository.existsByEmail(userRequestDto.getEmail())){
            throw new UserExistsException("이미 존재하는 이메일 입니다.");
        }
        String password = passwordEncoder.encode(userRequestDto.getPassword());
        UserRole role = Optional.ofNullable(userRequestDto.getUserRole())
                .orElse(UserRole.USER);
        UserRole.of(String.valueOf(role));

        User user = new User(
                userRequestDto.getEmail(),
                password,
                userRequestDto.getNickname(),
                role
        );
        userRepository.save(user);
        notificationService.sendRealTimeNotification("회원가입 성공","사용자가 회원가입에 성공했습니다.");
        return UserResponseDto.from(user);

    }

    @Transactional
    @Override
    public String login(UserRequestDto userRequestDto) {
        // 사용자 이메일로 사용자 찾기
        User user = userRepository.findByEmailOrElseThrow(userRequestDto.getEmail());

        // 비밀번호 확인
        if (!passwordEncoder.matches(userRequestDto.getPassword(), user.getPassword())) {
            throw new NotMatchPassword("비밀번호가 일치하지 않습니다.");
        }

        // JWT 토큰 생성
        String token = jwtUtil.createToken(user.getEmail(),user.getUserRole());
        notificationService.sendRealTimeNotification("로그인 성공","사용자가 로그인 했습니다.");
        return token;
    }

    @Transactional
    @Override
    public void deleteUser(String email, String password) {
        User user = userRepository.findByEmailOrElseThrow(email);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new NotMatchPassword("비밀번호가 일치 하지 않습니다.");
        }
        user.deleteAccount();
        userRepository.save(user);

    }
}
