package com.nbacm.trelldochi.domain.workspace.entity;

import com.nbacm.trelldochi.domain.board.entity.Board;
import com.nbacm.trelldochi.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "workspace")
public class WorkSpace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @OneToMany(mappedBy = "workSpace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkSpaceMember> members;

    @OneToMany(mappedBy = "workSpace", cascade = CascadeType.ALL)
    private List<Board> boards;

    public WorkSpace(String name, String description,User user) {
        this.name = name;
        this.description = description;
        this.members.add(new WorkSpaceMember(user,this));
    }

}
