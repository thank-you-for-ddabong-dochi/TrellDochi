package com.nbacm.trelldochi.domain.card.exception;

import com.nbacm.trelldochi.domain.common.exception.BadRequestException;

public class CardManagerAlreadyExistException extends BadRequestException {
    public CardManagerAlreadyExistException() {
        super("이미 존재하는 담당자입니다.");
    }
}
