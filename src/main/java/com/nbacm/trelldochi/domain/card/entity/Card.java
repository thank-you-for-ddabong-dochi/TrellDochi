package com.nbacm.trelldochi.domain.card.entity;

import com.nbacm.trelldochi.domain.attachment.entity.Attachment;
import com.nbacm.trelldochi.domain.comment.entity.Comment;
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


    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();


    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL)
    private List<Attachment> attachmentList = new ArrayList<>();

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL)
    private List<CardManager> managerList = new ArrayList<>();

}
