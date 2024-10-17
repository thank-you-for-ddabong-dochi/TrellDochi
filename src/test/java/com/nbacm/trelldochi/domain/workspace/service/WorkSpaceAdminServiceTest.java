package com.nbacm.trelldochi.domain.workspace.service;

import com.nbacm.trelldochi.domain.board.repository.BoardRepository;
import com.nbacm.trelldochi.domain.notifications.service.NotificationService;
import com.nbacm.trelldochi.domain.user.entity.User;
import com.nbacm.trelldochi.domain.user.entity.UserRole;
import com.nbacm.trelldochi.domain.user.repository.UserRepository;
import com.nbacm.trelldochi.domain.workspace.dto.WorkSpaceRequestDto;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class WorkSpaceAdminServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private WorkSpaceRepository workSpaceRepository;
    @Mock
    private WorkSpaceMemberRepository workSpaceMemberRepository;
    @Mock
    private BoardRepository boardRepository;
    @Mock
    private NotificationService notificationService;
    @InjectMocks
    private WorkSpaceAdminServiceImpl workSpaceAdminService;
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

    @DisplayName("워크 스페이스 생성 성공")
    @Test
    public void createWorkSpace() {
        //given
        WorkSpaceRequestDto requestDto = new WorkSpaceRequestDto("워크1", "설명1");

        given(userRepository.findByEmailOrElseThrow(user.getEmail()))
                .willReturn(user);
        given(workSpaceRepository.save(any(WorkSpace.class)))
                .willReturn(workSpace);

        //when
        WorkSpaceResponseDto result = workSpaceAdminService.createWorkSpace(user.getEmail(), requestDto);

        //then
        assertNotNull(result);
        assertEquals("워크1", result.getName());
        assertEquals("설명1", result.getDescription());
    }

    @DisplayName("워크 스페이스 업데이트 성공")
    @Test
    public void updateWorkSpace() {
        //given
        WorkSpaceRequestDto requestDto = new WorkSpaceRequestDto("변경1", "변경1");

        given(workSpaceRepository.findByUserEmailAndIdOrElseThrow(user.getEmail(), 1L))
                .willReturn(workSpace);
        given(userRepository.findByEmailOrElseThrow(user.getEmail()))
                .willReturn(user);
        ReflectionTestUtils.setField(workSpace, "name", "변경1");
        ReflectionTestUtils.setField(workSpace, "description", "변경1");
        given(workSpaceRepository.save(any(WorkSpace.class)))
                .willReturn(workSpace);
        //when
        WorkSpaceResponseDto result = workSpaceAdminService.updateWorkSpace(user.getEmail(), requestDto, 1L);

        //then
        assertNotNull(result);
        assertEquals("변경1", result.getName());
        assertEquals("변경1", result.getDescription());
    }

    @DisplayName("워크 스페이스 삭제 성공")
    @Test
    public void deleteWorkSpace() {
        //given
        given(workSpaceRepository.findById(1L))
                .willReturn(Optional.ofNullable(workSpace));
        given(userRepository.findByEmailOrElseThrow(user.getEmail()))
                .willReturn(user);
        //when
        workSpaceAdminService.deleteWorkSpace(user.getEmail(), 1L);
        //then
        verify(workSpaceRepository).deleteRelationBoards(1L);
    }

    @DisplayName("워크 스페이스가 없을 경우 NFE 던짐")
    @Test
    public void workSpaceNotFound(){

    }

}
