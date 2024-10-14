package com.nbacm.trelldochi.domain.user.repository;
import com.nbacm.trelldochi.domain.user.entity.User;
import com.nbacm.trelldochi.domain.user.exception.UserNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    default User findByEmailOrElseThrow(String email) {
        return findByEmail(email).orElseThrow(()-> new UserNotFoundException("유저를 찾을 수 없습니다"));
    }
}
