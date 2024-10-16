package com.nbacm.trelldochi.domain.attachment.dto;

import com.nbacm.trelldochi.domain.attachment.entity.Attachment;
import lombok.Getter;

@Getter
public class AttachmentResponseDto {
    private Long attachmentId;
    private String fileName;

    public AttachmentResponseDto(Attachment attachment){
        attachmentId = attachment.getId();
        this.fileName = attachment.getFileName();
    }
}
