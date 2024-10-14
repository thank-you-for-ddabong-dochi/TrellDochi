package com.nbacm.trelldochi.domain.card.repository;

import com.nbacm.trelldochi.domain.card.entity.Card;

import java.util.Optional;

public interface CardDslRepository {
    Optional<Card> findCardAndCommentsById(Long cardId);
}
