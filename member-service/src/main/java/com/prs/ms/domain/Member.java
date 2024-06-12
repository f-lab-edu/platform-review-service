package com.prs.ms.domain;


import com.prs.ms.type.MemberRole;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "members")
public class Member {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "member_id")
    private Long Id;

    @Column(unique = true, length = 15, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(value = EnumType.STRING)
    private MemberRole role;


    public Member(String username, String password, MemberRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

}
