package com.nbacm.trelldochi.domain.comment.exception;

import com.nbacm.trelldochi.domain.common.exception.ForbiddenException;

public class CommentForbiddenException extends ForbiddenException {
    public CommentForbiddenException() {
        super("권한이 없습니다.");
    }
}
