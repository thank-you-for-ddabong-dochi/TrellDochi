package com.nbacm.trelldochi.domain.comment.entity;

import com.nbacm.trelldochi.domain.card.entity.Card;
import com.nbacm.trelldochi.domain.comment.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "comment")
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;

    private boolean isDeleted = false;

    private String userEmail;

    public Comment(String userEmail, Card findCard, CommentRequestDto commentRequestDto) {
        this.userEmail = userEmail;
        title = commentRequestDto.getTitle();
        contents = commentRequestDto.getComments();
        card = findCard;
    }

    public Comment putComment(CommentRequestDto commentRequestDto) {
        title = commentRequestDto.getTitle() == null ? title : commentRequestDto.getTitle();
        contents = commentRequestDto.getComments() == null ? contents : commentRequestDto.getComments();

        return this;
    }

    public void deleteComment() {
        isDeleted = true;
    }
}
