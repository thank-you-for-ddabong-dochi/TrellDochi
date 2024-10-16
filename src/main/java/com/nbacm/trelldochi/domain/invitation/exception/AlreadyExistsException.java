package com.nbacm.trelldochi.domain.invitation.exception;

import com.nbacm.trelldochi.domain.common.exception.BadRequestException;

public class AlreadyExistsException extends BadRequestException {
    public AlreadyExistsException(String message) {
        super(message);
    }
}
