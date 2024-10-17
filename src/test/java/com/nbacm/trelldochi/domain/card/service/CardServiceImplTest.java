package com.nbacm.trelldochi.domain.card.service;

import com.nbacm.trelldochi.domain.card.dto.CardOneResponseDto;
import com.nbacm.trelldochi.domain.card.dto.CardPatchRequestDto;
import com.nbacm.trelldochi.domain.card.dto.CardRequestDto;
import com.nbacm.trelldochi.domain.card.dto.CardResponseDto;
import com.nbacm.trelldochi.domain.card.entity.Card;
import com.nbacm.trelldochi.domain.card.entity.CardManager;
import com.nbacm.trelldochi.domain.card.entity.CardStatus;
import com.nbacm.trelldochi.domain.card.repository.CardManagerRepository;
import com.nbacm.trelldochi.domain.card.repository.CardRepository;
import com.nbacm.trelldochi.domain.comment.repository.CommentRepository;
import com.nbacm.trelldochi.domain.common.dto.CustomUserDetails;
import com.nbacm.trelldochi.domain.list.entity.TodoList;
import com.nbacm.trelldochi.domain.list.repository.TodoRepository;
import com.nbacm.trelldochi.domain.notifications.service.NotificationService;
import com.nbacm.trelldochi.domain.user.entity.User;
import com.nbacm.trelldochi.domain.user.entity.UserRole;
import com.nbacm.trelldochi.domain.user.repository.UserRepository;
import com.nbacm.trelldochi.domain.workspace.entity.MemberRole;
import com.nbacm.trelldochi.domain.workspace.entity.WorkSpace;
import com.nbacm.trelldochi.domain.workspace.entity.WorkSpaceMember;
import com.nbacm.trelldochi.domain.workspace.repository.WorkSpaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cglib.core.Local;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceImplTest {

    @Mock
    CardRepository cardRepository;

    @Mock
    CommentRepository commentRepository;

    @Mock
    WorkSpaceRepository workSpaceRepository;

    @Mock
    TodoRepository todoRepository;

    @Mock
    CardManagerRepository cardManagerRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    CardViewService cardViewService;

    @Mock
    NotificationService notificationService;

    @InjectMocks
    CardServiceImpl cardService;

    Long userId = 1L;
    Long workspaceId = 1L;
    Long todoListId = 1L;
    User user;
    CustomUserDetails customUserDetails;
    CardRequestDto cardRequestDto;
    WorkSpace workSpace;

    @BeforeEach
    void setUp() {
        user = new User("1@naver.com", "Aa88888888!", "nickname", UserRole.ADMIN);
        ReflectionTestUtils.setField(user, "id", userId);

        customUserDetails = new CustomUserDetails(user);

        cardRequestDto = new CardRequestDto("title", "explanation", LocalDate.of(2024, 12, 01), "OPEN");

        workSpace = new WorkSpace("workspace", "description", user);


    }

    @Test
    void createCard_success() {
        // given
        // workspace member를 mock으로 등록
        WorkSpaceMember workSpaceMember = mock(WorkSpaceMember.class);

        // workspace 접근 권한 확인하기
        given(workSpaceRepository.findByUserEmailAndWorkspaceId(anyString(), eq(workspaceId)))
                .willReturn(Optional.of(workSpaceMember));

        // 권한이 'READONLY'가 아닌지 확인
        given(workSpaceMember.getRole()).willReturn(MemberRole.ADMIN);

        TodoList todoList = mock(TodoList.class);
        Card savedCard = new Card(todoList, cardRequestDto);
        ReflectionTestUtils.setField(savedCard, "id", userId);

        given(todoRepository.findById(todoListId)).willReturn(Optional.of(todoList));
        given(cardRepository.save(any(Card.class))).willReturn(savedCard);
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));
        given(cardManagerRepository.save(any(CardManager.class))).willReturn(new CardManager(user, savedCard));

        // when
        CardResponseDto result = cardService.createCard(customUserDetails, workspaceId, todoListId, cardRequestDto);

        // then
        assertNotNull(result);
        assertEquals(1, result.getCardId());
        assertEquals("title", result.getTitle());
        assertEquals("OPEN", result.getCardStatus().toString());
        verify(cardRepository, times(1)).save(any(Card.class));
    }

    @Test
    void getCard_success() {
        // given
        // 조회할 카드 생성
        Long cardId = 1L;
        TodoList todoList = mock(TodoList.class);
        Card card = new Card(todoList, cardRequestDto);
        ReflectionTestUtils.setField(card, "id", userId);

        given(cardRepository.findCardAndCommentsById(cardId)).willReturn(Optional.of(card));

        // when
        CardOneResponseDto result = cardService.getCard(cardId);

        // then
        assertNotNull(result);
        verify(cardRepository, times(1)).findCardAndCommentsById(cardId);
    }

    @Test
    void getCardWithCache() {
    }

    @Test
    void getCardRanking() {
    }

    @Test
    void putCard_success() {
        // given
        Long cardId = 1L;
        // 변경할 내용
        CardPatchRequestDto cardPatchRequestDto = new CardPatchRequestDto("title", "변경 완료", LocalDate.of(2024, 12, 01), CardStatus.ING);

        TodoList todoList = mock(TodoList.class);
        Card card = new Card(todoList, cardRequestDto);
        ReflectionTestUtils.setField(card, "id", userId);

        given(cardRepository.findCardAndCommentsById(cardId)).willReturn(Optional.of(card));

        // workspace 권한 확인
        WorkSpaceMember workSpaceMember = mock(WorkSpaceMember.class);
        given(workSpaceRepository.findByUserEmailAndWorkspaceIdAndLock(anyString(), eq(workspaceId)))
                .willReturn(Optional.of(workSpaceMember));
        given(workSpaceMember.getRole()).willReturn(MemberRole.ADMIN);

        // when
        CardResponseDto result = cardService.putCard(customUserDetails, workspaceId, cardId, cardPatchRequestDto);

        // then
        assertNotNull(result);
        assertEquals(1L, result.getCardId());
        assertEquals("title", result.getTitle());
        assertEquals("변경 완료", result.getExplanation());
        assertEquals("ING", result.getCardStatus().toString());
    }

    @Test
    void deleteCard() {
        // given
        Long cardId = 1L;
        TodoList todoList = mock(TodoList.class);
        Card findCard = new Card(todoList, cardRequestDto);
        ReflectionTestUtils.setField(findCard, "id", userId);

        given(cardRepository.findCardAndCommentsById(cardId)).willReturn(Optional.of(findCard));

        // workspace 권한 확인
        WorkSpaceMember workSpaceMember = mock(WorkSpaceMember.class);
        given(workSpaceRepository.findByUserEmailAndWorkspaceId(anyString(), eq(workspaceId)))
                .willReturn(Optional.of(workSpaceMember));
        given(workSpaceMember.getRole()).willReturn(MemberRole.ADMIN);

        doNothing().when(commentRepository).deleteAllWithCardId(anyLong());

        // when
        cardService.deleteCard(customUserDetails, workspaceId, cardId);


    }

    @Test
    void addManager() {
    }

    @Test
    void patchCardStatus() {
    }

    @Test
    void searchCards() {
    }
}