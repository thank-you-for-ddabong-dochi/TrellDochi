package com.nbacm.trelldochi.domain.workspace.repository;

import com.nbacm.trelldochi.domain.workspace.entity.WorkSpaceMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@RequestMapping
public interface WorkSpaceMemberRepository extends JpaRepository<WorkSpaceMember, Long> {

    Optional<WorkSpaceMember> findByUserIdAndWorkspaceId(Long userId, Long workspaceId);
}
