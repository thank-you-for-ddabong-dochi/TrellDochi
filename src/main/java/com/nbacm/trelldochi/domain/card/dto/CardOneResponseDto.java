package com.nbacm.trelldochi.domain.card.dto;

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
    private List<CommentResponseDto> commentList;

    public CardOneResponseDto(Card savedCard) {
        title = savedCard.getTitle();
        explanation = savedCard.getExplanation();
        deadline = savedCard.getDeadline();
        cardStatus = savedCard.getStatus();
        this.commentList = savedCard.getCommentList().stream().map(CommentResponseDto::new).toList();
    }
}
