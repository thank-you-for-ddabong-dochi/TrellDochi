package com.nbacm.trelldochi.domain.invitation.exception;

import com.nbacm.trelldochi.domain.common.exception.NotFoundException;

public class InvitationNotFoundException extends NotFoundException {
    public InvitationNotFoundException(String message) {
        super(message);
    }
}
