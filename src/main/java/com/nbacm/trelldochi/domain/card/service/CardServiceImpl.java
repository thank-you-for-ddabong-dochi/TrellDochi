package com.nbacm.trelldochi.domain.card.service;

import com.nbacm.trelldochi.domain.card.dto.*;
import com.nbacm.trelldochi.domain.card.entity.Card;
import com.nbacm.trelldochi.domain.card.entity.CardManager;
import com.nbacm.trelldochi.domain.card.entity.CardStatus;
import com.nbacm.trelldochi.domain.card.exception.CardForbiddenException;
import com.nbacm.trelldochi.domain.card.exception.CardManagerAlreadyExistException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final CommentRepository commentRepository;
    private final WorkSpaceRepository workSpaceRepository;
    private final TodoRepository todoRepository;
    private final CardManagerRepository cardManagerRepository;
    private final UserRepository userRepository;
    private final CardViewService cardViewService;

    @Override
    @Transactional
    public CardResponseDto createCard(CustomUserDetails customUserDetails, Long workspaceId, Long todoListId, CardRequestDto cardRequestDto) {
        // workspace 접근 권한 환인하기
        isAuthInWorkSpace(customUserDetails, workspaceId);

        // 어떤 리스트 아래에 있는 카드인지 찾기
        TodoList findTodoList = todoRepository.findById(todoListId).orElseThrow();

        // 카드 저장하기
        Card saveCard = cardRepository.save(new Card(findTodoList, cardRequestDto));

        // 담당자 테이블(CardManager)에 저장하기
        User user = userRepository.findByEmail(customUserDetails.getEmail())
                .orElseThrow(()-> new UserNotFoundException("user를 찾을 수 없습니다."));

        cardManagerRepository.save(new CardManager(user, saveCard));

        return new CardResponseDto(saveCard);
    }

    @Override
    @Transactional
    public CardOneResponseDto getCard(Long cardId) {

        Card findCard = findCard(cardId);

        cardViewService.incrementCardViewCount(findCard);

        return new CardOneResponseDto(findCard);
    }

    @Override
    @Transactional
    public CardResponseDto putCard(CustomUserDetails customUserDetails, Long workspaceId, Long cardId, CardPatchRequestDto cardPatchRequestDto) {
        Card findCard = findCard(cardId);

        // workspace 접근 권한 환인하기
        isAuthInWorkSpace(customUserDetails, workspaceId);

        return new CardResponseDto(findCard.putCard(cardPatchRequestDto));

    }

    @Override
    @Transactional
    public void deleteCard(CustomUserDetails customUserDetails, Long workspaceId, Long cardId) {
        Card findCard = findCard(cardId);

        // workspace 접근 권한 환인하기
        isAuthInWorkSpace(customUserDetails, workspaceId);

        commentRepository.deleteAllWithCardId(cardId);

        findCard.deleteCard();
    }

    @Override
    @Transactional
    public CardOneResponseDto addManager(CustomUserDetails userDetails, Long cardId, CardManagerRequestDto cardManagerRequestDto) {
        // 글 담당자만 추가할 수 있도록 구현했습니다.
        // 로그인 한 유저의 id 찾기
        User findUser = userRepository.findByEmailOrElseThrow(userDetails.getEmail());

        // 카드 담당자인지 찾습니다.
        CardManager findCardManager = cardRepository.findUserInUserList(cardId, findUser.getId()).orElseThrow(CardForbiddenException::new);

        // 이미 존재하는지 확인하기
        Optional<CardManager> cardManager = cardRepository.findUserInUserList(cardId, cardManagerRequestDto.getUserId());
        if (cardManager.isPresent()) {
            throw new CardManagerAlreadyExistException();
        }

        // 추가할 사람 찾기
        User addUser = userRepository.findById(cardManagerRequestDto.getUserId()).orElseThrow(() -> new UserNotFoundException("유저를 찾을 수 없습니다"));

        Card findCard = findCard(cardId);;

        cardManagerRepository.save(new CardManager(addUser, findCard));

        return new CardOneResponseDto(findCard);
    }

    @Override
    @Transactional
    public CardOneResponseDto patchCardStatus(CustomUserDetails userDetails, Long cardId, CardStatusRequestDto cardStateRequestDto) {
        // 로그인 한 유저의 id 찾기
        User findUser = userRepository.findByEmailOrElseThrow(userDetails.getEmail());

        // 카드 담당자인지 찾습니다.
        CardManager findCardManager = cardRepository.findUserInUserList(cardId, findUser.getId()).orElseThrow(CardForbiddenException::new);

        // card status가 맞는지 확인하기
        CardStatus cardStatus = CardStatus.of(cardStateRequestDto.getStatus());

        Card findCard = findCard(cardId);

        Card patchCard = findCard.patchCardState(cardStatus);

        return new CardOneResponseDto(patchCard);
    }

    @Override
    public Page<Card> searchCards(String title, String explanation, LocalDate deadline, String managerName, Long boardId, Pageable pageable) {
        return cardRepository.searchCards(title, explanation, deadline, managerName, boardId, pageable);
    }

    private WorkSpaceMember isAuthInWorkSpace(CustomUserDetails customUserDetails, Long workspaceId){
        // 워크 스페이스에 권한이 없는 경우(추가가 안된 경우)
        WorkSpaceMember findWorkSpaceMember = workSpaceRepository.findByUserEmailAndWorkspaceId(customUserDetails.getEmail(), workspaceId)
                .orElseThrow(() -> new WorkSpaceAccessDeniedException("work space에 접근 권한이 없습니다."));

        // 권한이 readonly인 경우 생성 불가
        if (findWorkSpaceMember.getRole() == MemberRole.READONLY) {
            throw new WorkSpaceAccessDeniedException("work space에 접근 권한이 없습니다.");
        }

        return findWorkSpaceMember;
    }

    private Card findCard(Long cardId) {
        Card findCard = cardRepository.findCardAndCommentsById(cardId).orElseThrow(CardNotFoundException::new);

        if (findCard.isDeleted()) {
            throw new CardNotFoundException();
        }
        return findCard;
    }
}

