package com.nbacm.trelldochi.domain.card.exception;

import com.nbacm.trelldochi.domain.common.exception.NotFoundException;

public class CardNotFoundException extends NotFoundException {
    public CardNotFoundException() {
        super("카드를 찾을 수 없습니다.");
    }
}
