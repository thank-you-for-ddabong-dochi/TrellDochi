package com.nbacm.trelldochi.domain.card.dto;

import com.nbacm.trelldochi.domain.attachment.dto.AttachmentResponseDto;
import com.nbacm.trelldochi.domain.card.entity.Card;
import com.nbacm.trelldochi.domain.card.entity.CardStatus;
import com.nbacm.trelldochi.domain.comment.dto.CommentResponseDto;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class CardOneResponseDto {

    private String title;
    private String explanation;
    private LocalDate deadline;
    private CardStatus cardStatus;
    private List<CardManagerResponseDto> cardManagerResponseDtoList;
    private List<CommentResponseDto> commentList;
    private List<AttachmentResponseDto> attachmentList;

    public CardOneResponseDto(Card savedCard) {
        title = savedCard.getTitle();
        explanation = savedCard.getExplanation();
        deadline = savedCard.getDeadline();
        cardStatus = savedCard.getStatus();
        this.cardManagerResponseDtoList = savedCard.getManagerList().stream().map(CardManagerResponseDto::new).toList();
        this.commentList = savedCard.getCommentList().stream().map(CommentResponseDto::new).toList();
        this.attachmentList = savedCard.getAttachmentList().stream().map(AttachmentResponseDto::new).toList();
    }
}
