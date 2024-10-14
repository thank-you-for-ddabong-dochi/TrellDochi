package com.nbacm.trelldochi.domain.user.entity;

import com.nbacm.trelldochi.domain.card.entity.CardManager;
import com.nbacm.trelldochi.domain.workspace.entity.WorkSpaceMember;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private Boolean isDelete = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<WorkSpaceMember> workSpaceMembers;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CardManager> cardManagers;


    public User(String email,UserRole userRole) {
        this.email = email;
        this.userRole = userRole;
    }

    public User(String email, String password, String nickname, UserRole userRole) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.userRole = userRole;
    }
    public void deleteAccount(){
        this.isDelete = true;
    }

}
