package com.nbacm.trelldochi.domain.attachment.entity;

import com.nbacm.trelldochi.domain.card.entity.Card;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "attachment")
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
    private Card card;

}
