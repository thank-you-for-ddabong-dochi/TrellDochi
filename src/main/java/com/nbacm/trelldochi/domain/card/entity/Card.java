package com.nbacm.trelldochi.domain.card.entity;

import com.nbacm.trelldochi.domain.attachment.entity.Attachment;
import com.nbacm.trelldochi.domain.card.dto.CardPatchRequestDto;
import com.nbacm.trelldochi.domain.card.dto.CardRequestDto;
import com.nbacm.trelldochi.domain.comment.entity.Comment;
import com.nbacm.trelldochi.domain.list.entity.TodoList;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "card")
@NoArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String explanation;

    @Column(nullable = false)
    private LocalDate deadline;

    @Enumerated(EnumType.STRING)
    private CardStatus status;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL)
    private List<Attachment> attachmentList = new ArrayList<>();

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL)
    private List<CardManager> managerList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todolist_id")
    private TodoList todolist;

    public Card(CardRequestDto cardRequestDto) {
        this.title = cardRequestDto.getTitle();
        this.explanation = cardRequestDto.getExplanation();
        this.deadline = cardRequestDto.getDeadline();
        this.status = CardStatus.of(cardRequestDto.getStatus());
    }

    public Card putCard(CardPatchRequestDto cardPatchRequestDto) {
        title = cardPatchRequestDto.getTitle() == null ? title : cardPatchRequestDto.getTitle();
        explanation = cardPatchRequestDto.getExplanation() == null ? explanation : cardPatchRequestDto.getExplanation();
        deadline = cardPatchRequestDto.getDeadline() == null ? deadline : cardPatchRequestDto.getDeadline();
        status = cardPatchRequestDto.getCardStatus() == null ? status : cardPatchRequestDto.getCardStatus();
        return this;
    }

    public void deleteCard() {
        isDeleted = true;
    }
}
