package com.nbacm.trelldochi.domain.workspace.entity;

import jakarta.persistence.*;
import com.nbacm.trelldochi.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class WorkSpaceMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "workspace_id")
    private WorkSpace workSpace;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    public WorkSpaceMember(User user, WorkSpace workSpace, MemberRole role) {
        this.user = user;
        this.workSpace = workSpace;
        this.role = role;
        user.getWorkSpaceMembers().add(this);
    }
}
