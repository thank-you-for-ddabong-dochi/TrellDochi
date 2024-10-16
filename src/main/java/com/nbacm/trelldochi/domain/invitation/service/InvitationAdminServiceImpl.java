package com.nbacm.trelldochi.domain.invitation.service;

import com.nbacm.trelldochi.domain.invitation.dto.InvitationRequestDto;
import com.nbacm.trelldochi.domain.invitation.dto.InvitationResponseDto;
import com.nbacm.trelldochi.domain.invitation.entity.WorkSpaceInvitation;
import com.nbacm.trelldochi.domain.invitation.enums.INVITATION_STATUS;
import com.nbacm.trelldochi.domain.invitation.exception.AlreadyExistsException;
import com.nbacm.trelldochi.domain.invitation.exception.InvitationNotFoundException;
import com.nbacm.trelldochi.domain.invitation.exception.InvitationPermissionException;
import com.nbacm.trelldochi.domain.invitation.repository.InvitationRepository;
import com.nbacm.trelldochi.domain.user.entity.User;
import com.nbacm.trelldochi.domain.user.repository.UserRepository;
import com.nbacm.trelldochi.domain.workspace.entity.MemberRole;
import com.nbacm.trelldochi.domain.workspace.repository.WorkSpaceMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InvitationAdminServiceImpl implements InvitationAdminService {

    private final UserRepository userRepository;
    private final InvitationRepository invatationRepository;
    private final WorkSpaceMemberRepository workSpaceMemberRepository;

    @Override
    @Transactional
    public InvitationResponseDto inviteMember(String email, Long workspaceId, InvitationRequestDto requestDto) {
        User user = userRepository.findByEmailOrElseThrow(email);
        User receivedUser = userRepository.findByEmailOrElseThrow(requestDto.getEmail());

        boolean exists = workSpaceMemberRepository.existsByWorkspaceIdAndUserId(workspaceId, receivedUser.getId());
        if (exists) {
            throw new AlreadyExistsException("워크스페이스에 멤버가 이미 존재합니다.");
        }
        exists = invatationRepository.existsByWorkspaceIdAndReceivedUserIdAndRequestUserId(workspaceId, receivedUser.getId(), user.getId());
        if (exists) {
            throw new AlreadyExistsException("이미 초대가 요청된 멤버입니다.");
        }

        WorkSpaceInvitation workSpaceInvitation = new WorkSpaceInvitation(
                workspaceId,
                user.getId(),
                receivedUser.getId(),
                INVITATION_STATUS.INVITING,
                MemberRole.of(requestDto.getRole())
        );
        WorkSpaceInvitation savedInvitation = invatationRepository.save(workSpaceInvitation);
        return new InvitationResponseDto(savedInvitation);
    }

    @Override
    @Transactional
    public void cancleInvitation(String email, Long workspaceId, InvitationRequestDto requestDto) {
        User user = userRepository.findByEmailOrElseThrow(email);
        User receivedUser = userRepository.findByEmailOrElseThrow(requestDto.getEmail());
        WorkSpaceInvitation workSpaceInvitation = invatationRepository.findByWorkspaceIdAndReceivedUserIdAndRequestUserId(workspaceId, receivedUser.getId(), user.getId()).orElseThrow(
                () -> new InvitationNotFoundException("초대 만료")
        );
        if (!user.getId().equals(workSpaceInvitation.getRequestUserId())) {
            throw new InvitationPermissionException("접근 권한이 없습니다.");
        }
        invatationRepository.delete(workSpaceInvitation);
    }
}
