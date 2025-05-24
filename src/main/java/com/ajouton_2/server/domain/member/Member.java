package com.ajouton_2.server.domain.member;

import com.ajouton_2.server.domain.groupmember.GroupMember;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String studentId;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String department;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupMember> groupmembers = new ArrayList<>();

    @Builder
    public Member(String email, String password, String name, String studentId,
                  String phoneNumber, String department) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.studentId = studentId;
        this.phoneNumber = phoneNumber;
        this.department = department;
    }
}
