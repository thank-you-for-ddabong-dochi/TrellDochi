package com.nbacm.trelldochi.domain.card.repository;

import com.nbacm.trelldochi.domain.card.entity.Card;
import com.nbacm.trelldochi.domain.card.entity.CardManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

public interface CardDslRepository {
    Optional<Card> findCardAndCommentsById(Long cardId);

    Optional<CardManager> findUserInUserList(Long cardId, Long userId);

    Page<Card> searchCards(String title, String explanation, LocalDate deadline, String managerName, Long boardId, Pageable pageable);
}
