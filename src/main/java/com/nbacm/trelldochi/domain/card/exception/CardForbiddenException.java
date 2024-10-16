package com.nbacm.trelldochi.domain.card.exception;

import com.nbacm.trelldochi.domain.common.exception.ForbiddenException;

public class CardForbiddenException extends ForbiddenException {
    public CardForbiddenException() {
        super("Card에 접근 권한이 없습니다.");
    }
}
