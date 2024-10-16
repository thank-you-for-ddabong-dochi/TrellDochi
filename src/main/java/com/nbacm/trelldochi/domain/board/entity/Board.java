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

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private String background;

    @OneToMany(mappedBy = "board")
    private List<TodoList> todoLists;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private WorkSpace workSpace;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    public Board(String title, String contents, WorkSpace workSpace) {
        this.title = title;
        this.contents = contents;
        this.workSpace = workSpace;
    }

    public void update(BoardRequestDto boardRequestDto) {
        this.title = boardRequestDto.getTitle();
        this.contents = boardRequestDto.getContents();
        this.todoLists = boardRequestDto.getTodoLists();
    }

    public void addList(TodoList todoList) {
        this.todoLists.add(todoList);
    }

    public void delete() {
        this.isDeleted = true;
    }
}
