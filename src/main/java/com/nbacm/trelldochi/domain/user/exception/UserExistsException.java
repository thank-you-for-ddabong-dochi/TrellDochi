package com.nbacm.trelldochi.domain.user.exception;

import com.nbacm.trelldochi.domain.common.exception.BadRequestException;

public class UserExistsException extends BadRequestException {
    public UserExistsException(String message) {
        super(message);
    }
}
