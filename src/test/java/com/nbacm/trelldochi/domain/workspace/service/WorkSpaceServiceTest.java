package com.nbacm.trelldochi.domain.workspace.service;

import com.nbacm.trelldochi.domain.invitation.repository.InvitationRepository;
import com.nbacm.trelldochi.domain.user.entity.User;
import com.nbacm.trelldochi.domain.user.entity.UserRole;
import com.nbacm.trelldochi.domain.user.repository.UserRepository;
import com.nbacm.trelldochi.domain.workspace.dto.WorkSpaceResponseDto;
import com.nbacm.trelldochi.domain.workspace.entity.WorkSpace;
import com.nbacm.trelldochi.domain.workspace.repository.WorkSpaceMemberRepository;
import com.nbacm.trelldochi.domain.workspace.repository.WorkSpaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class WorkSpaceServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private WorkSpaceRepository workSpaceRepository;
    @Mock
    private WorkSpaceMemberRepository workSpaceMemberRepository;
    @Mock
    private InvitationRepository invitationRepository;
    @InjectMocks
    private WorkSpaceServiceImpl workSpaceService;
    private WorkSpace workSpace;
    private WorkSpace workSpace2;
    private User user;
    String email;

    @BeforeEach
    public void setUp() {
        user = new User(email, "dnwls111", "dndn", UserRole.ADMIN);
        workSpace = new WorkSpace("워크1", "설명1", user);
        workSpace2 = new WorkSpace("워크2", "설명1", user);
        email = "dnwls111@naver.com";

        ReflectionTestUtils.setField(workSpace, "id", 1L);
        ReflectionTestUtils.setField(workSpace2, "id", 2L);
        ReflectionTestUtils.setField(user, "id", 1L);
    }

    @DisplayName("워크 스페이스 조회 성공")
    @Test
    public void getWorkspace() {
        //given
        given(workSpaceRepository.findByUserEmailAndIdOrElseThrow(anyString(), anyLong()))
                .willReturn(workSpace);

        //when
        WorkSpaceResponseDto workSpaceResponse = workSpaceService.getWorkSpace(email, 1L);

        //then

        assertEquals(workSpaceResponse.getId(), 1L);
        assertEquals(workSpaceResponse.getName(), "워크1");
    }

    @DisplayName("워크 스페이스 다건 조회 성공")
    @Test
    public void getWorkspaces() {
        //given
        List<WorkSpaceResponseDto> workSpaces = new ArrayList<>();
        workSpaces.add(new WorkSpaceResponseDto(workSpace));
        workSpaces.add(new WorkSpaceResponseDto(workSpace2));

        Pageable pageable = PageRequest.of(0, 10);
        Page<WorkSpaceResponseDto> workSpacePage = new PageImpl<>(workSpaces, pageable, 2);

        given(userRepository.findByEmailOrElseThrow(anyString()))
                .willReturn(user);
        given(workSpaceRepository.findWorkSpacesByUserId(user.getId(), pageable))
                .willReturn(workSpacePage);

        //when
        Page<WorkSpaceResponseDto> result = workSpaceService.getWorkSpaces(email, 1, 10);

        //then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("워크1", result.getContent().get(0).getName());
        assertEquals("워크2", result.getContent().get(1).getName());
    }

}
