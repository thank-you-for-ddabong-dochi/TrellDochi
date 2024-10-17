package com.nbacm.trelldochi.domain.attachment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nbacm.trelldochi.domain.card.entity.Card;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "attachment")
@NoArgsConstructor
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    @JsonIgnore
    private Card card;

    public Attachment(String fileName, String url, Card findCard) {
        this.fileName = fileName;
        this.url = url;
        this.card = findCard;
    }
}
