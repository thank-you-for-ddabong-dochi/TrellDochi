package com.nbacm.trelldochi.domain.comment.exception;

import com.nbacm.trelldochi.domain.common.exception.NotFoundException;

public class CommentNotFoundException extends NotFoundException {
    public CommentNotFoundException() {
        super("댓글을 찾을 수 없습니다.");
    }
}
