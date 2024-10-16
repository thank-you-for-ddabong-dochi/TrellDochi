package com.nbacm.trelldochi.domain.card.service;

import com.nbacm.trelldochi.domain.card.dto.*;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public interface CardService {

    CardResponseDto createCard(CustomUserDetails customUserDetails, Long workspaceId, Long todoListId, CardRequestDto cardRequestDto);

    CardOneResponseDto getCard(Long cardId);

    CardOneResponseDto getCardWithCache(Long cardId, CustomUserDetails customUserDetails);

    CardResponseDto putCard(CustomUserDetails customUserDetails, Long workspaceId, Long cardId, CardPatchRequestDto cardPatchRequestDto);

    void deleteCard(CustomUserDetails customUserDetails, Long workspaceId, Long cardId);

    CardOneResponseDto addManager(CustomUserDetails userDetails, Long cardId, CardManagerRequestDto cardManagerRequestDto);

    CardOneResponseDto patchCardStatus(CustomUserDetails userDetails, Long cardId, CardStatusRequestDto cardStateRequestDto);

    Page<Card> searchCards(String title, String explanation, LocalDate deadline, String managerName, Long boardId, Pageable pageable);

    List<CardRankingResponseDto> getCardRanking(int topN);
}
