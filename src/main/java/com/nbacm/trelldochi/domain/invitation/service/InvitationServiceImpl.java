package com.nbacm.trelldochi.domain.invitation.service;

import com.nbacm.trelldochi.domain.invitation.enums.INVITATION_STATUS;
import com.nbacm.trelldochi.domain.invitation.exception.InvitationNotFoundException;
import com.nbacm.trelldochi.domain.invitation.exception.InvitationPermissionException;
import com.nbacm.trelldochi.domain.invitation.repository.InvitationRepository;
import com.nbacm.trelldochi.domain.user.entity.User;
import com.nbacm.trelldochi.domain.user.repository.UserRepository;
import com.nbacm.trelldochi.domain.invitation.entity.WorkSpaceInvitation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InvitationServiceImpl implements InvitationService {
    private final UserRepository userRepository;
    private final InvitationRepository invitationRepository;

    @Override
    @Transactional
    public void rejectInvitation(String email, Long invitationId) {
        User user = userRepository.findByEmailOrElseThrow(email);
        WorkSpaceInvitation workSpaceInvitation = invitationRepository.findById(invitationId).orElseThrow(
                () -> new InvitationNotFoundException("초대 만료")
        );
        if (!user.getId().equals(workSpaceInvitation.getReceivedUserId())) {
            throw new InvitationPermissionException("접근 권한이 없습니다.");
        }
        workSpaceInvitation.changeStatus(INVITATION_STATUS.ACCEPTED);
    }

    @Override
    @Transactional
    public void acceptInvitation(String email, Long invitationId) {
        User user = userRepository.findByEmailOrElseThrow(email);
        WorkSpaceInvitation workSpaceInvitation = invitationRepository.findById(invitationId).orElseThrow(
                () -> new InvitationNotFoundException("초대 만료")
        );
        if (!user.getId().equals(workSpaceInvitation.getReceivedUserId())) {
            throw new InvitationPermissionException("접근 권한이 없습니다.");
        }
        workSpaceInvitation.changeStatus(INVITATION_STATUS.REJECTED);
    }
}
