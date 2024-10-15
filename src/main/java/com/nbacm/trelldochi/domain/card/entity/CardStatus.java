package com.nbacm.trelldochi.domain.card.entity;

import com.sun.jdi.request.InvalidRequestStateException;

import java.util.Arrays;

public enum CardStatus {
    OPEN, ING, CLOSE;

    public static CardStatus of(String type) {
        return Arrays.stream(CardStatus.values())
                .filter(r -> r.name().equalsIgnoreCase(type))
                .findFirst()
                .orElseThrow(() -> new InvalidRequestStateException("유효하지 않은 MenuType 입니다."));
    }
}
