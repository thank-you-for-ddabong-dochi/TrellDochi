package com.nbacm.trelldochi.domain.workspace.exception;

import com.nbacm.trelldochi.domain.common.exception.NotFoundException;

public class WorkSpaceMemberNotFoundException extends NotFoundException {

    public WorkSpaceMemberNotFoundException(String message) {
        super(message);
    }
}
