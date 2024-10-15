package com.nbacm.trelldochi.domain.card.service;

import com.nbacm.trelldochi.domain.card.dto.CardOneResponseDto;
import com.nbacm.trelldochi.domain.card.dto.CardPatchRequestDto;
import com.nbacm.trelldochi.domain.card.dto.CardRequestDto;
import com.nbacm.trelldochi.domain.card.dto.CardResponseDto;
import com.nbacm.trelldochi.domain.card.entity.Card;
import com.nbacm.trelldochi.domain.card.exception.CardNotFoundException;
import com.nbacm.trelldochi.domain.card.repository.CardRepository;
import com.nbacm.trelldochi.domain.comment.entity.Comment;
import com.nbacm.trelldochi.domain.comment.repository.CommentRepository;
import com.nbacm.trelldochi.domain.common.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final CommentRepository commentRepository;

    public CardResponseDto createCard(AuthUser authUser, CardRequestDto cardRequestDto) {
        return new CardResponseDto(cardRepository.save(new Card(cardRequestDto)));
    }

    public CardOneResponseDto getCard(Long cardId) {
        Card findCard = cardRepository.findCardAndCommentsById(cardId).orElseThrow(CardNotFoundException::new);
        if (findCard.isDeleted()) {
            throw new CardNotFoundException();
        }
        return new CardOneResponseDto(findCard);
    }

    @Transactional
    public CardResponseDto putCard(AuthUser authUser, Long cardId, CardPatchRequestDto cardPatchRequestDto) {
        Card findCard = cardRepository.findById(cardId).orElseThrow(CardNotFoundException::new);
        if (findCard.isDeleted()) {
            throw new CardNotFoundException();
        }

        return new CardResponseDto(findCard.putCard(cardPatchRequestDto));
    }

    @Transactional
    public void deleteCard(AuthUser authUser, Long cardId) {
        Card findCard = cardRepository.findCardAndCommentsById(cardId).orElseThrow(CardNotFoundException::new);

        if (findCard.isDeleted()) {
            throw new CardNotFoundException();
        }

        for (Comment comment : findCard.getCommentList()) {
            comment.deleteComment();
        }

        findCard.deleteCard();
    }
}
