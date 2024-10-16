package com.nbacm.trelldochi.domain.attachment.dto;

import com.nbacm.trelldochi.domain.attachment.entity.Attachment;
import lombok.Getter;

@Getter
public class AttachmentResponseDto {
    private String fileName;

    public AttachmentResponseDto(Attachment attachment){
        this.fileName = attachment.getFileName();
    }
}
