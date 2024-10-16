package com.nbacm.trelldochi.domain.attachment.exception;

import com.nbacm.trelldochi.domain.common.exception.NotFoundException;

public class AttachmentNotFoundException extends NotFoundException {
    public AttachmentNotFoundException() {
        super("첨부 파일을 찾을 수 없습니다.");
    }
}
