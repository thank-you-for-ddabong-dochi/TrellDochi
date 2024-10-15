package com.nbacm.trelldochi.domain.workspace.exception;

import com.nbacm.trelldochi.domain.common.exception.ForbiddenException;

public class WorkSpaceAccessDeniedException extends ForbiddenException {
    public WorkSpaceAccessDeniedException(String message) {
        super(message);
    }
}
