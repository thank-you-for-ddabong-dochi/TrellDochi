package com.nbacm.trelldochi.domain.workspace.entity;

import jakarta.persistence.*;
import com.nbacm.trelldochi.domain.user.entity.User;
import lombok.Getter;

@Getter
@Entity
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

}
