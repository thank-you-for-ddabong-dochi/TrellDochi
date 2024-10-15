package com.nbacm.trelldochi.domain.workspace.exception;

import com.nbacm.trelldochi.domain.common.exception.NotFoundException;

public class WorkSpaceNotFoundException extends NotFoundException {
    public WorkSpaceNotFoundException(String message) {
        super(message);
    }
}
