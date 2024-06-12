package com.prs.rs.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
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

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Long platformId;


    public Review(Long platformId, Long memberId, String content, Byte star) {
        this.content = content;
        this.star = star;
        this.memberId = memberId;
        this.platformId = platformId;
    }

    public Review changeInfo(String content, Byte star) {
        this.content = content;
        this.star = star;
        return this;
    }
}
