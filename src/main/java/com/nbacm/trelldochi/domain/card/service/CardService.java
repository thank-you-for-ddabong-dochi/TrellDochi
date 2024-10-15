package com.nbacm.trelldochi.domain.card.service;

import com.nbacm.trelldochi.domain.card.dto.CardOneResponseDto;
import com.nbacm.trelldochi.domain.card.dto.CardPatchRequestDto;
import com.nbacm.trelldochi.domain.card.dto.CardRequestDto;
import com.nbacm.trelldochi.domain.card.dto.CardResponseDto;
import com.nbacm.trelldochi.domain.card.entity.Card;
import com.nbacm.trelldochi.domain.card.entity.CardManager;
import com.nbacm.trelldochi.domain.card.exception.CardNotFoundException;
import com.nbacm.trelldochi.domain.card.repository.CardManagerRepository;
import com.nbacm.trelldochi.domain.card.repository.CardRepository;
import com.nbacm.trelldochi.domain.comment.entity.Comment;
import com.nbacm.trelldochi.domain.comment.repository.CommentRepository;
import com.nbacm.trelldochi.domain.common.dto.CustomUserDetails;
import com.nbacm.trelldochi.domain.list.entity.TodoList;
import com.nbacm.trelldochi.domain.list.repository.TodoRepository;
import com.nbacm.trelldochi.domain.user.entity.User;
import com.nbacm.trelldochi.domain.user.exception.UserNotFoundException;
import com.nbacm.trelldochi.domain.user.repository.UserRepository;
import com.nbacm.trelldochi.domain.workspace.entity.MemberRole;
import com.nbacm.trelldochi.domain.workspace.entity.WorkSpaceMember;
import com.nbacm.trelldochi.domain.workspace.exception.WorkSpaceAccessDeniedException;
import com.nbacm.trelldochi.domain.workspace.repository.WorkSpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final CommentRepository commentRepository;
    private final WorkSpaceRepository workSpaceRepository;
    private final TodoRepository todoRepository;
    private final CardManagerRepository cardManagerRepository;
    private final UserRepository userRepository;

    public CardResponseDto createCard(CustomUserDetails customUserDetails, Long workspaceId, Long todoListId, CardRequestDto cardRequestDto) {
        // 워크 스페이스에 권한이 없는 경우(추가가 안된 경우)
        WorkSpaceMember findWorkSpaceMember = workSpaceRepository.findByUserEmailAndWorkspaceId(customUserDetails.getEmail(), workspaceId).orElseThrow(() -> new WorkSpaceAccessDeniedException("work space에 접근 권한이 없습니다."));

        // 권한이 readonly인 경우 생성 불가
        if (findWorkSpaceMember.getRole() == MemberRole.READONLY) {
            throw new WorkSpaceAccessDeniedException("work space에 접근 권한이 없습니다.");
        }

        // 어떤 리스트 아래에 있는 카드인지 찾기
        TodoList findTodoList = todoRepository.findById(todoListId).orElseThrow();

        // 카드 저장하기
        Card saveCard = cardRepository.save(new Card(findTodoList, cardRequestDto));

        // 담당자 테이블(CardManager)에 저장하기
        User user = userRepository.findByEmail(customUserDetails.getEmail()).orElseThrow(()-> new UserNotFoundException("user를 찾을 수 없습니다."));
        cardManagerRepository.save(new CardManager(user, saveCard));

        return new CardResponseDto(saveCard);
    }

    public CardOneResponseDto getCard(Long cardId) {
        Card findCard = cardRepository.findCardAndCommentsById(cardId).orElseThrow(CardNotFoundException::new);
        if (findCard.isDeleted()) {
            throw new CardNotFoundException();
        }
        return new CardOneResponseDto(findCard);
    }

    @Transactional
    public CardResponseDto putCard(CustomUserDetails customUserDetails, Long workspaceId, Long cardId, CardPatchRequestDto cardPatchRequestDto) {
        Card findCard = cardRepository.findById(cardId).orElseThrow(CardNotFoundException::new);

        // 워크 스페이스에 권한이 없는 경우(추가가 안된 경우)
        WorkSpaceMember findWorkSpaceMember = workSpaceRepository.findByUserEmailAndWorkspaceId(customUserDetails.getEmail(), workspaceId).orElseThrow(() -> new WorkSpaceAccessDeniedException("work space에 접근 권한이 없습니다."));

        // 권한이 readonly인 경우 생성 불가
        if (findWorkSpaceMember.getRole() == MemberRole.READONLY) {
            throw new WorkSpaceAccessDeniedException("work space에 접근 권한이 없습니다.");
        }

        if (findCard.isDeleted()) {
            throw new CardNotFoundException();
        }

            return new CardResponseDto(findCard.putCard(cardPatchRequestDto));

    }

    @Transactional
    public void deleteCard(CustomUserDetails customUserDetails, Long workspaceId, Long cardId) {
        Card findCard = cardRepository.findCardAndCommentsById(cardId).orElseThrow(CardNotFoundException::new);

        if (findCard.isDeleted()) {
            throw new CardNotFoundException();
        }

        // 워크 스페이스에 권한이 없는 경우(추가가 안된 경우)
        WorkSpaceMember findWorkSpaceMember = workSpaceRepository.findByUserEmailAndWorkspaceId(customUserDetails.getEmail(), workspaceId).orElseThrow(() -> new WorkSpaceAccessDeniedException("work space에 접근 권한이 없습니다."));

        // 권한이 readonly인 경우 생성 불가
        if (findWorkSpaceMember.getRole() == MemberRole.READONLY) {
            throw new WorkSpaceAccessDeniedException("work space에 접근 권한이 없습니다.");
        }

        for (Comment comment : findCard.getCommentList()) {
            comment.deleteComment();
        }

        findCard.deleteCard();
    }
}
