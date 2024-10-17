package com.nbacm.trelldochi.domain.workspace.service;

import com.nbacm.trelldochi.domain.board.repository.BoardRepository;
import com.nbacm.trelldochi.domain.common.exception.ForbiddenException;
import com.nbacm.trelldochi.domain.notifications.service.NotificationService;
import com.nbacm.trelldochi.domain.user.entity.User;
import com.nbacm.trelldochi.domain.user.entity.UserRole;
import com.nbacm.trelldochi.domain.user.repository.UserRepository;
import com.nbacm.trelldochi.domain.workspace.dto.WorkSpaceMemberResponseDto;
import com.nbacm.trelldochi.domain.workspace.dto.WorkSpaceRequestDto;
import com.nbacm.trelldochi.domain.workspace.dto.WorkSpaceResponseDto;
import com.nbacm.trelldochi.domain.workspace.entity.MemberRole;
import com.nbacm.trelldochi.domain.workspace.entity.WorkSpace;
import com.nbacm.trelldochi.domain.workspace.entity.WorkSpaceMember;
import com.nbacm.trelldochi.domain.workspace.exception.WorkSpaceAccessDeniedException;
import com.nbacm.trelldochi.domain.workspace.exception.WorkSpaceMemberNotFoundException;
import com.nbacm.trelldochi.domain.workspace.exception.WorkSpaceNotFoundException;
import com.nbacm.trelldochi.domain.workspace.repository.WorkSpaceMemberRepository;
import com.nbacm.trelldochi.domain.workspace.repository.WorkSpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkSpaceAdminServiceImpl implements WorkSpaceAdminService {

    private final UserRepository userRepository;
    private final WorkSpaceRepository workSpaceRepository;
    private final WorkSpaceMemberRepository workSpaceMemberRepository;
    private final BoardRepository boardRepository;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public WorkSpaceResponseDto createWorkSpace(String email, WorkSpaceRequestDto requestDto) {
        User user = userRepository.findByEmailOrElseThrow(email);
        //워크 스페이스는 ADMIN권한을 가진 유저만 생성
        if (!user.getUserRole().equals(UserRole.ADMIN)) {
            throw new ForbiddenException("ADMIN 권한이 없습니다.");
        }
        //이름, 설명
        WorkSpace workSpace = new WorkSpace(
                requestDto.getName(),
                requestDto.getDescription(),
                user
        );

        WorkSpace savedWorkSpace = workSpaceRepository.save(workSpace);
        notificationService.sendRealTimeNotification("워크 스페이스 생성",workSpace.getName().toString()+" 워크 스페이스가 생성 되었습니다.");
        return new WorkSpaceResponseDto(savedWorkSpace);
    }

    @Override
    @Transactional
    public WorkSpaceResponseDto updateWorkSpace(String email, WorkSpaceRequestDto requestDto, Long workspaceId) {
        WorkSpace workSpace = workSpaceRepository.findByUserEmailAndIdOrElseThrow(email, workspaceId);
        User user = userRepository.findByEmailOrElseThrow(email);
        isOwner(user.getId(), workSpace.getOwner().getId());
        workSpace.update(requestDto);
        WorkSpace updatedWorkSpace = workSpaceRepository.save(workSpace);
        notificationService.sendRealTimeNotification("워크 스페이스 수정",workSpace.getName().toString()+"워크스페이스가 수정이 되었습니다.");
        return new WorkSpaceResponseDto(updatedWorkSpace);
    }

    @Override
    @Transactional
    public void deleteWorkSpace(String email, Long workspaceId) {
        WorkSpace workSpace = workSpaceRepository.findById(workspaceId).orElseThrow(
                () -> new WorkSpaceNotFoundException("워크 스페이스가 없습니다.")
        );
        User user = userRepository.findByEmailOrElseThrow(email);
        isOwner(user.getId(), workSpace.getOwner().getId());
        workSpaceRepository.deleteRelationBoards(workspaceId);
    }

    @Override
    @Transactional
    public WorkSpaceMemberResponseDto changeMemberRole(String email, Long workspaceId, Long memberId, String role) {
        validatePermission(email, workspaceId);
        WorkSpace workSpace = workSpaceRepository.findById(workspaceId).orElseThrow(()
                -> new WorkSpaceNotFoundException("워크 스페이스가 없습니다.")
        );
        isNotOwner(memberId, workSpace.getOwner().getId());

        WorkSpaceMember workSpaceMember = workSpaceMemberRepository.findByUserIdAndWorkspaceId(memberId, workspaceId).orElseThrow(
                () -> new WorkSpaceMemberNotFoundException("워크 스페이스 멤버가 존재하지 않습니다.")
        );
        workSpaceMember.changeRole(role);
        WorkSpaceMember savedWorkSpaceMember = workSpaceMemberRepository.save(workSpaceMember);
        notificationService.sendRealTimeNotification("멤버 변경","멤버가 변경 되었습니다.");
        return new WorkSpaceMemberResponseDto(workSpaceMember);
    }

    @Override
    @Transactional
    public void deleteMember(String email, Long workspaceId, Long memberId) {
        validatePermission(email, workspaceId);
        WorkSpaceMember workSpaceMember = workSpaceMemberRepository.findByUserIdAndWorkspaceId(memberId, workspaceId).orElseThrow(
                () -> new WorkSpaceNotFoundException("워크 스페이스가 존재하지 않습니다.")
        );
        WorkSpace workSpace = workSpaceRepository.findById(workspaceId).orElseThrow(()
                -> new WorkSpaceNotFoundException("워크 스페이스가 없습니다.")
        );
        isNotOwner(memberId, workSpace.getOwner().getId());

        workSpaceMemberRepository.delete(workSpaceMember);
    }

    private void validatePermission(String email, Long workspaceId) {
        User user = userRepository.findByEmailOrElseThrow(email);
        WorkSpaceMember workSpaceMember = workSpaceMemberRepository.findByUserIdAndWorkspaceId(user.getId(), workspaceId).orElseThrow(
                () -> new WorkSpaceNotFoundException("워크 스페이스가 존재하지 않습니다.")
        );
        if (!workSpaceMember.getRole().equals(MemberRole.ADMIN)) {
            throw new WorkSpaceAccessDeniedException("워크 스페이스 ADMIN 권한이 없습니다.");
        }
    }

    //오너만 workspace의 수정, 삭제 가능하게 수정
    private void isOwner(Long userid, Long ownerId) {
        if (!ownerId.equals(userid)) {
            throw new WorkSpaceAccessDeniedException("워크 스페이스 소유자가 아닙니다.");
        };
    }
    private void isNotOwner(Long userid, Long ownerId){
        if (ownerId.equals(userid)) {
            throw new WorkSpaceAccessDeniedException("워크 스페이스 소유자는 수정할 수 없습니다..");
        };
    }
}
