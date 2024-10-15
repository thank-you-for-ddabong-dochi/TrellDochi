package com.nbacm.trelldochi.domain.invitation.service;

import com.nbacm.trelldochi.domain.invitation.dto.InvitationRequestDto;
import com.nbacm.trelldochi.domain.invitation.dto.InvitationResponseDto;

public interface InvitationAdminService {
    InvitationResponseDto inviteMember(String email, Long workspaceId, InvitationRequestDto requestDto);

    void cancleInvitation(String email, Long invitationId);
}
