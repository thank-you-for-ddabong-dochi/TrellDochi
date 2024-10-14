package com.nbacm.trelldochi.domain.user.dto;

import com.nbacm.trelldochi.domain.user.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
    @Email(message = "유효한 이메일 형식이 어야 합니다.")
    private String email;


    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    private String password;


    @NotBlank(message = "닉네임은 필수 입력 항목 입니다.")
    private String nickname;

    private UserRole userRole;

}
