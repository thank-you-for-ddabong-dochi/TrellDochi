package com.nbacm.trelldochi.domain.invitation.exception;

import com.nbacm.trelldochi.domain.common.exception.BadRequestException;

public class BadInvitationRequestException extends BadRequestException {

    public BadInvitationRequestException(String message) {
        super(message);
    }
}
