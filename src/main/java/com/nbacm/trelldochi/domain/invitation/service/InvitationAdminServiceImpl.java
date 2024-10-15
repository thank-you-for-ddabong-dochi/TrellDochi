package com.nbacm.trelldochi.domain.invitation.service;

import com.nbacm.trelldochi.domain.invitation.enums.INVITATION_STATUS;
import com.nbacm.trelldochi.domain.invitation.exception.InvitationNotFoundException;
import com.nbacm.trelldochi.domain.invitation.exception.InvitationPermissionException;
import com.nbacm.trelldochi.domain.invitation.repository.InvitationRepository;
import com.nbacm.trelldochi.domain.user.entity.User;
import com.nbacm.trelldochi.domain.user.repository.UserRepository;
import com.nbacm.trelldochi.domain.invitation.dto.InvitationRequestDto;
import com.nbacm.trelldochi.domain.invitation.dto.InvitationResponseDto;
import com.nbacm.trelldochi.domain.invitation.entity.WorkSpaceInvitation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvitationAdminServiceImpl implements InvitationAdminService {

    private final UserRepository userRepository;
    private final InvitationRepository invatationRepository;

    @Override
    public InvitationResponseDto inviteMember(String email, Long workspaceId, InvitationRequestDto requestDto) {
        User user = userRepository.findByEmailOrElseThrow(email);
        User receivedUser = userRepository.findByEmailOrElseThrow(requestDto.getEmail());
        WorkSpaceInvitation workSpaceInvitation = new WorkSpaceInvitation(
                workspaceId,
                user.getId(),
                receivedUser.getId(),
                INVITATION_STATUS.INVITING
        );
        WorkSpaceInvitation savedInvitation = invatationRepository.save(workSpaceInvitation);
        return new InvitationResponseDto(savedInvitation);
    }

    @Override
    public void cancleInvitation(String email, Long invitationId) {
        User user = userRepository.findByEmailOrElseThrow(email);
        WorkSpaceInvitation workSpaceInvitation = invatationRepository.findById(invitationId).orElseThrow(
                () -> new InvitationNotFoundException("초대 만료")
        );
        if (!user.getId().equals(workSpaceInvitation.getRequestUserId())) {
            throw new InvitationPermissionException("접근 권한이 없습니다.");
        }
        workSpaceInvitation.changeStatus(INVITATION_STATUS.CANCELED);
    }
}
