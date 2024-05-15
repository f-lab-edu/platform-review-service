package com.review.rsproject.domain;


import com.review.rsproject.type.PlatformStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

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

    @Column(nullable = false)
    private Byte star;


    @Enumerated(EnumType.STRING)
    private PlatformStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "platform")
    private List<Review> reviews = new ArrayList<>();


    public Platform(String name, String url, String description, Member member) {
        this.name = name;
        this.url = url;
        this.description = description;
        this.member = member;
        this.status = PlatformStatus.WAIT;
        this.star = 0;
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

    public Platform updateStar(Long reviewCount, Long totalStar) {
        this.star = (byte) (totalStar / reviewCount);
        return this;
    }

}
