package com.prs.ps.domain;

import com.prs.ps.type.PlatformStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
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

    @Column(nullable = false, name = "avg_score")
    private Integer avgScore;

    @Column(nullable = false, name = "total_score")
    private Long totalScore;

    @Column(nullable = false, name = "review_count")
    private Long reviewCount;


    @Enumerated(EnumType.STRING)
    private PlatformStatus status;


    private Long memberId;


    @Version
    private Long version;


    public Platform(String name, String url, String description, Long memberId) {
        this.name = name;
        this.url = url;
        this.description = description;
        this.memberId = memberId;
        this.status = PlatformStatus.WAIT;
        this.avgScore = 0;
        this.totalScore = 0L;
        this.reviewCount = 0L;
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


    public static Platform getEmpty() {
        return new Platform();
    }

    private void refreshAvgScore() {
        if (this.reviewCount == 0L) {
            this.avgScore = 0;
        } else {
            long result = this.totalScore / this.reviewCount;
            this.avgScore = (int) result;
        }

    }

    public void addScore(Integer score) {
        ++this.reviewCount;
        this.totalScore += score;
        refreshAvgScore();
    }

    public void subScore(Integer score) {
        --this.reviewCount;
        this.totalScore -= score;
        refreshAvgScore();
    }

    public void updateScore(Integer beforeScore, Integer afterScore) {
        this.totalScore -= beforeScore;
        this.totalScore += afterScore;
        refreshAvgScore();
    }
}
