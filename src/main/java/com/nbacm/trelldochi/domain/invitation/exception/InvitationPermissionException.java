package com.nbacm.trelldochi.domain.invitation.exception;

import com.nbacm.trelldochi.domain.common.exception.ForbiddenException;

public class InvitationPermissionException extends ForbiddenException {

    public InvitationPermissionException(String message) {
        super(message);
    }
}
