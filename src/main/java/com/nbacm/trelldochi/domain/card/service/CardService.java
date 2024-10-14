package com.nbacm.trelldochi.domain.card.service;

import com.nbacm.trelldochi.domain.card.dto.CardRequestDto;
import com.nbacm.trelldochi.domain.card.dto.CardResponseDto;
import com.nbacm.trelldochi.domain.card.entity.Card;
import com.nbacm.trelldochi.domain.card.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;

    public CardResponseDto createCard(CardRequestDto cardRequestDto) {
        Card card = new Card(cardRequestDto);
        Card savedCard = cardRepository.save(card);
        return new CardResponseDto(savedCard);
    }
}
