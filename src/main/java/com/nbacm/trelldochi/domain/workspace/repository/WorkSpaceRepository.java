package com.nbacm.trelldochi.domain.workspace.repository;

import com.nbacm.trelldochi.domain.workspace.entity.WorkSpace;
import com.nbacm.trelldochi.domain.workspace.exception.WorkSpaceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WorkSpaceRepository extends JpaRepository<WorkSpace, Long>, WorkSpaceQueryDslRepository {
    @Query("SELECT w FROM WorkSpace w " +
            "JOIN WorkSpaceMember wm " +
            "ON w.id = wm.id " +
            "JOIN User u " +
            "ON wm.user.id = u.id " +
            "WHERE u.email = :email " +
            "AND w.id = :id "
    )
    Optional<WorkSpace> findByUserEmailAndId(@Param("email") String email,
                                             @Param("id") Long Id);

    default WorkSpace findByUserEmailAndIdOrElseThrow(String email, Long id) {
        return findByUserEmailAndId(email, id).orElseThrow(() -> new WorkSpaceNotFoundException("워크스페이스를 찾을 수 없습니다."));
    }

    @Query("SELECT COUNT(wm) > 0 " +
            "FROM WorkSpaceMember wm " +
            "JOIN User u " +
            "ON u.id = wm.user.id " +
            "WHERE wm.id = :workspaceId " +
            "AND u.email = :email"
    )
    boolean isUserWorkSpaceOwner(String email, Long workspaceId);
}
