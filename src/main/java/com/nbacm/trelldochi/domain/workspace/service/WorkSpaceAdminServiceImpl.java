package com.nbacm.trelldochi.domain.workspace.service;

import com.nbacm.trelldochi.domain.common.exception.ForbiddenException;
import com.nbacm.trelldochi.domain.user.entity.User;
import com.nbacm.trelldochi.domain.user.entity.UserRole;
import com.nbacm.trelldochi.domain.user.repository.UserRepository;
import com.nbacm.trelldochi.domain.workspace.dto.WorkSpaceRequestDto;
import com.nbacm.trelldochi.domain.workspace.dto.WorkSpaceResponseDto;
import com.nbacm.trelldochi.domain.workspace.entity.WorkSpace;
import com.nbacm.trelldochi.domain.workspace.repository.WorkSpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkSpaceAdminServiceImpl implements WorkSpaceAdminService {

    private final UserRepository userRepository;
    private final WorkSpaceRepository workSpaceRepository;

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

        return new WorkSpaceResponseDto(
                savedWorkSpace.getId(),
                savedWorkSpace.getName(),
                savedWorkSpace.getDescription()
        );
    }

    @Override
    @Transactional
    public WorkSpaceResponseDto updateWorkSpace(String email, WorkSpaceRequestDto requestDto, Long workspaceId) {
        WorkSpace workSpace = workSpaceRepository.findByUserEmailAndIdOrElseThrow(email, workspaceId);

        WorkSpace updatedWorkSpace = workSpaceRepository.save(workSpace.update(requestDto));

        return new WorkSpaceResponseDto(
                updatedWorkSpace.getId(),
                updatedWorkSpace.getName(),
                updatedWorkSpace.getDescription()
        );
    }
}
