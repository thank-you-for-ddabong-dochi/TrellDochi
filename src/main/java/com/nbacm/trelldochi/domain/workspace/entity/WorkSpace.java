package com.nbacm.trelldochi.domain.workspace.entity;

import com.nbacm.trelldochi.domain.board.entity.Board;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
@Table(name = "workspace")
public class WorkSpace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;

    @OneToMany(mappedBy = "workSpace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkSpaceMember> members;

    @OneToMany(mappedBy = "board")
    private List<Board> boards;
}
