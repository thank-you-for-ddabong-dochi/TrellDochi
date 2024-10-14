package com.nbacm.trelldochi.domain.invitation.service;

public interface InvitationService {
    void rejectInvitation(String email, Long invitationId);
    void acceptInvitation(String email, Long invitationId);
}
