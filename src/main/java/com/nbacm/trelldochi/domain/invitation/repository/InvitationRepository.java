package com.nbacm.trelldochi.domain.invitation.repository;

import com.nbacm.trelldochi.domain.invitation.entity.WorkSpaceInvitation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitationRepository extends JpaRepository<WorkSpaceInvitation,Long> {
}
