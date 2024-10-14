package com.nbacm.trelldochi.domain.user.exception;

import com.nbacm.trelldochi.domain.common.exception.BadRequestException;

public class NotMatchPassword extends BadRequestException {
    public NotMatchPassword(String message) {
        super(message);
    }
}
