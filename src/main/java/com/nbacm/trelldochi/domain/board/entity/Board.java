package com.nbacm.trelldochi.domain.board.entity;

import com.nbacm.trelldochi.domain.board.dto.BoardRequestDto;
import com.nbacm.trelldochi.domain.list.entity.TodoList;
import com.nbacm.trelldochi.domain.workspace.entity.WorkSpace;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "board")
public class Board {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String backgroundColor = "#FFFFFF";

    @Column
    private String backgroundImageUrl;

    @OneToMany(mappedBy = "board")
    private List<TodoList> todoLists;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private WorkSpace workSpace;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    public Board(String title, String backgroundColor, String backgroundImageUrl, WorkSpace workSpace) {
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.backgroundImageUrl = backgroundImageUrl;
        this.workSpace = workSpace;
    }

    public void update(BoardRequestDto boardRequestDto) {
        this.title = boardRequestDto.getTitle();
        this.backgroundColor = boardRequestDto.getBackgroundColor();
        this.backgroundImageUrl = boardRequestDto.getBackgroundImageUrl();
        this.todoLists = boardRequestDto.getTodoLists();
    }

    public void addList(TodoList todoList) {
        this.todoLists.add(todoList);
    }

    public void delete() {
        this.isDeleted = true;
    }
}
