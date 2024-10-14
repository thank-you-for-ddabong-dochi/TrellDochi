package com.nbacm.trelldochi.domain.list.entity;

import com.nbacm.trelldochi.domain.board.entity.Board;
import com.nbacm.trelldochi.domain.card.entity.Card;
import com.nbacm.trelldochi.domain.list.dto.TodoListRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "todolist")
public class TodoList {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private int listOrder;

    @OneToMany(mappedBy = "todolist")
    private List<Card> cardList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    public TodoList(String title, int listOrder, Board board) {
        this.title = title;
        this.listOrder = listOrder;
        this.board = board;
    }

    public void update(TodoListRequestDto todoListRequestDto) {
        this.title = todoListRequestDto.getTitle();
        this.listOrder = todoListRequestDto.getListOrder();
        this.cardList = todoListRequestDto.getCardList();
    }

    public void addCard(Card card) {
        this.cardList.add(card);
    }

    public void move(int order) {
        this.listOrder = order;
    }

    public void delete() {
        this.isDeleted = true;
    }
}
