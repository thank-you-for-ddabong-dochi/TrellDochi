package com.nbacm.trelldochi.domain.invitation.repository;

import com.nbacm.trelldochi.domain.invitation.entity.WorkSpaceInvitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvitationRepository extends JpaRepository<WorkSpaceInvitation,Long> {
    Optional<WorkSpaceInvitation> findByWorkspaceIdAndReceivedUserId(Long invitationId, Long id);
    Optional<WorkSpaceInvitation> findByWorkspaceIdAndReceivedUserIdAndRequestUserId(Long workspaceId, Long receiveId,Long requestId);

    boolean existsByWorkspaceIdAndReceivedUserIdAndRequestUserId(Long workspaceId, Long id, Long id1);
}
