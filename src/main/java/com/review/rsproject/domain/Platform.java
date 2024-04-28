package com.review.rsproject.domain;


import com.review.rsproject.type.PlatformStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Platform extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "platform_id")
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 50, nullable = false)
    private String url;

    @Column(length = 50, nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    private PlatformStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;


    public Platform(String name, String url, String description, Member member) {
        this.name = name;
        this.url = url;
        this.description = description;
        this.member = member;
        this.status = PlatformStatus.WAIT;
    }

    public Platform changeInfo(String description, PlatformStatus status) {
        if (description != null) {
            this.description = description;
        }
        if (status != null) {
            this.status = status;
        }
        return this;
    }

}
