package com.nbacm.trelldochi.domain.user.repository;

import com.nbacm.trelldochi.domain.common.exception.NotFoundException;
import com.nbacm.trelldochi.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    default User findByEmailOrElseThrow(String email) {
        return findByEmail(email).orElseThrow(()-> new NotFoundException("유저를 찾을 수 없습니다"));
    }
}
