package com.nbacm.trelldochi.domain.invitation.entity;

import com.nbacm.trelldochi.domain.invitation.enums.INVITATION_STATUS;
import com.nbacm.trelldochi.domain.invitation.exception.BadInvitationRequestException;
import com.nbacm.trelldochi.domain.workspace.entity.MemberRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "workspace_invitation")
public class WorkSpaceInvitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long workspaceId;
    @Column(nullable = false)
    private Long requestUserId;
    @Column(nullable = false)
    private Long receivedUserId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private INVITATION_STATUS status;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role;

    public WorkSpaceInvitation(Long workspaceId, Long id, Long receivedUserId, INVITATION_STATUS status,MemberRole role) {
        this.workspaceId = workspaceId;
        this.requestUserId = id;
        this.receivedUserId = receivedUserId;
        this.status = status;
        this.role = role;
    }

    public void changeStatus(INVITATION_STATUS currentStatus, INVITATION_STATUS changeStatus) {
        if (currentStatus.equals(INVITATION_STATUS.INVITING)) {
            this.status = changeStatus;
            return;
        }
        throw new BadInvitationRequestException("변경할 수 있는 상태가 아닙니다.");
    }
}
