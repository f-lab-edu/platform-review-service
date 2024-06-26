package com.prs.rs.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Review extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(nullable = false)
    private Integer score;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Long platformId;


    public static Review getEmpty() {
        return new Review();
    }

    public Review(Long platformId, Long memberId, String content, Integer score) {
        this.content = content;
        this.score = score;
        this.memberId = memberId;
        this.platformId = platformId;
    }

    public void changeInfo(String content, Integer score) {
        this.content = content;
        this.score = score;
    }
}
