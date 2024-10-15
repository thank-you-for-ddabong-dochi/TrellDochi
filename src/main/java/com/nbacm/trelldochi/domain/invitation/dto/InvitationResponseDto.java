package com.nbacm.trelldochi.domain.invitation.dto;

import com.nbacm.trelldochi.domain.invitation.entity.WorkSpaceInvitation;
import lombok.Getter;

@Getter
public class InvitationResponseDto {
    private Long requestUserId;
    private Long receivedUserId;
    private String status;

    public InvitationResponseDto(WorkSpaceInvitation savedInvitation) {
        this.requestUserId = savedInvitation.getRequestUserId();
        this.receivedUserId = savedInvitation.getReceivedUserId();
        this.status = savedInvitation.getStatus().name();
    }
}
