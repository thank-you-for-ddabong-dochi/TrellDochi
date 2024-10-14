package com.nbacm.trelldochi.domain.workspace.entity;

import jakarta.persistence.*;
import com.nbacm.trelldochi.domain.user.entity.User;

@Entity
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
    private String role;

}
