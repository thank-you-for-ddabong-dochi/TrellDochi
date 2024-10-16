package com.nbacm.trelldochi.domain.invitation.service;

import com.nbacm.trelldochi.domain.invitation.entity.WorkSpaceInvitation;
import com.nbacm.trelldochi.domain.invitation.enums.INVITATION_STATUS;
import com.nbacm.trelldochi.domain.invitation.exception.InvitationNotFoundException;
import com.nbacm.trelldochi.domain.invitation.exception.InvitationPermissionException;
import com.nbacm.trelldochi.domain.invitation.repository.InvitationRepository;
import com.nbacm.trelldochi.domain.user.entity.User;
import com.nbacm.trelldochi.domain.user.repository.UserRepository;
import com.nbacm.trelldochi.domain.workspace.entity.WorkSpace;
import com.nbacm.trelldochi.domain.workspace.entity.WorkSpaceMember;
import com.nbacm.trelldochi.domain.workspace.exception.WorkSpaceNotFoundException;
import com.nbacm.trelldochi.domain.workspace.repository.WorkSpaceMemberRepository;
import com.nbacm.trelldochi.domain.workspace.repository.WorkSpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InvitationServiceImpl implements InvitationService {
    private final UserRepository userRepository;
    private final InvitationRepository invitationRepository;
    private final WorkSpaceMemberRepository workSpaceMemberRepository;
    private final WorkSpaceRepository workSpaceRepository;

    @Override
    @Transactional
    public void acceptInvitation(String email, Long workspaceId) {
        User user = userRepository.findByEmailOrElseThrow(email);
        WorkSpaceInvitation workSpaceInvitation = invitationRepository.findByWorkspaceIdAndReceivedUserId(workspaceId, user.getId()).orElseThrow(
                () -> new InvitationNotFoundException("초대 만료")
        );
        if (!user.getId().equals(workSpaceInvitation.getReceivedUserId())) {
            throw new InvitationPermissionException("접근 권한이 없습니다.");
        }
        workSpaceInvitation.changeStatus(workSpaceInvitation.getStatus(), INVITATION_STATUS.ACCEPTED);
        WorkSpace workSpace = workSpaceRepository.findById(workspaceId).orElseThrow(()
                -> new WorkSpaceNotFoundException("워크 스페이스가 없습니다.")
        );
        WorkSpaceMember workSpaceMember = new WorkSpaceMember(user, workSpace, workSpaceInvitation.getRole());
        workSpaceMemberRepository.save(workSpaceMember);
    }

    @Override
    @Transactional
    public void rejectInvitation(String email, Long workspaceId) {
        User user = userRepository.findByEmailOrElseThrow(email);
        WorkSpaceInvitation workSpaceInvitation = invitationRepository.findByWorkspaceIdAndReceivedUserId(workspaceId, user.getId()).orElseThrow(
                () -> new InvitationNotFoundException("초대 만료")
        );
        if (!user.getId().equals(workSpaceInvitation.getReceivedUserId())) {
            throw new InvitationPermissionException("접근 권한이 없습니다.");
        }
        invitationRepository.delete(workSpaceInvitation);
    }
}
