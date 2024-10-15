package com.nbacm.trelldochi.domain.workspace.entity;

import com.nbacm.trelldochi.domain.common.enums.DeleteState;
import com.nbacm.trelldochi.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "workspace_member")
public class WorkSpaceMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "workspace_id")
    private WorkSpace workspace;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Enumerated(value = EnumType.STRING)
    private DeleteState isDeleted;

    public WorkSpaceMember(User user, WorkSpace workSpace, MemberRole role) {
        this.user = user;
        this.workspace = workSpace;
        this.role = role;
        this.isDeleted = DeleteState.UNDELETED;
        user.getWorkSpaceMembers().add(this);
    }

    public void changeRole(String role) {
        this.role = MemberRole.valueOf(role);
    }

    public void delete() {
        this.isDeleted = DeleteState.DELETED;
    }
}
