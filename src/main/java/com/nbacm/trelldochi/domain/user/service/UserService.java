package com.nbacm.trelldochi.domain.user.service;

import com.nbacm.trelldochi.domain.user.dto.UserRequestDto;
import com.nbacm.trelldochi.domain.user.dto.UserResponseDto;

public interface UserService {

    UserResponseDto signUp(UserRequestDto userRequestDto);

    String login(UserRequestDto userRequestDto);

    void deleteUser(String email,String password);

}
