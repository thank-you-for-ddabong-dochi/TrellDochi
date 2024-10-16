package com.nbacm.trelldochi.domain.workspace.service;

import com.nbacm.trelldochi.domain.user.entity.User;
import com.nbacm.trelldochi.domain.user.repository.UserRepository;
import com.nbacm.trelldochi.domain.workspace.dto.WorkSpaceResponseDto;
import com.nbacm.trelldochi.domain.invitation.repository.InvitationRepository;
import com.nbacm.trelldochi.domain.workspace.repository.WorkSpaceMemberRepository;
import com.nbacm.trelldochi.domain.workspace.repository.WorkSpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class    WorkSpaceServiceImpl implements WorkSpaceService {

    private final UserRepository userRepository;
    private final WorkSpaceRepository workSpaceRepository;
    private final WorkSpaceMemberRepository workSpaceMemberRepository;
    private final InvitationRepository workSpaceInvitationRepository;

    @Override
    public Page<WorkSpaceResponseDto> getWorkSpaces(String email, int page, int size) {
        User user = userRepository.findByEmailOrElseThrow(email);
        Pageable pageable = PageRequest.of(page - 1, size);

        return workSpaceRepository.findWorkSpacesByUserId(user.getId(), pageable);
    }

    @Override
    public WorkSpaceResponseDto getWorkSpace(String email, Long workSpaceId) {
        return new WorkSpaceResponseDto(workSpaceRepository.findByUserEmailAndIdOrElseThrow(email, workSpaceId));
    }
}
