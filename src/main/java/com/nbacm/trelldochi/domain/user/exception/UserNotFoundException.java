package com.nbacm.trelldochi.domain.user.exception;

import com.nbacm.trelldochi.domain.common.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
