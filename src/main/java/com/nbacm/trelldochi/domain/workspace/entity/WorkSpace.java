package com.nbacm.trelldochi.domain.workspace.entity;

import com.nbacm.trelldochi.domain.board.entity.Board;
import com.nbacm.trelldochi.domain.user.entity.User;
import com.nbacm.trelldochi.domain.workspace.dto.WorkSpaceRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@SQLRestriction("is_deleted = false")
@Table(name = "workspace")
public class WorkSpace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkSpaceMember> members = new ArrayList<>();

    @OneToMany(mappedBy = "workSpace", cascade = CascadeType.ALL)
    private List<Board> boards;

    public WorkSpace(String name, String description, User user) {
        this.name = name;
        this.description = description;
        this.owner = user;
        this.members.add(new WorkSpaceMember(user, this, MemberRole.ADMIN));
    }

    public WorkSpace update(WorkSpaceRequestDto requestDto) {
        if (requestDto.getName() != null) {
            this.name = requestDto.getName();
        }
        if (requestDto.getDescription() != null) {
            this.description = requestDto.getDescription();
        }
        return this;
    }
    public void delete(){
        this.isDeleted = true;
    }
}
