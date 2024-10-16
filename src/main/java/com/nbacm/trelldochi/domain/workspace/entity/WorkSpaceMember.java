package com.nbacm.trelldochi.domain.workspace.entity;

import com.nbacm.trelldochi.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor
@SQLRestriction("is_deleted = false")
@Table(name = "workspace_member")
public class WorkSpaceMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private WorkSpace workspace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    public WorkSpaceMember(User user, WorkSpace workSpace, MemberRole role) {
        this.user = user;
        this.workspace = workSpace;
        this.role = role;
        user.getWorkSpaceMembers().add(this);
    }

    public void changeRole(String role) {
        this.role = MemberRole.valueOf(role);
    }
}
