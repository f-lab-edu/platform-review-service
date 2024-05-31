package com.review.rsproject.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Review extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(nullable = false)
    private Byte star;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "platform_id", nullable = false)
    private Platform platform;


    public Review(Platform platform, Member member, String content, Byte star) {
        this.content = content;
        this.star = star;
        this.member = member;
        this.platform = platform;

        platform.getReviews().add(this);
    }

    public Review changeInfo(String content, Byte star) {
        this.content = content;
        this.star = star;
        return this;
    }
}
