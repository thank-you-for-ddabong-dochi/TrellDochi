package com.nbacm.trelldochi.domain.workspace.repository;

import com.nbacm.trelldochi.domain.workspace.entity.WorkSpaceMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkSpaceMemberRepository extends JpaRepository<WorkSpaceMember, Long> {

    Optional<WorkSpaceMember> findByUserIdAndWorkspaceId(Long id, Long workspaceId);

    boolean existsByWorkspaceIdAndUserId(Long workspaceId, Long id);
}
