package com.prs.ps.domain;

import com.prs.ps.type.PlatformStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
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

    @Column(nullable = false)
    private Byte star;


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
        this.star = (reviewCount == 0L) ? (byte) 0 : (byte) (totalStar / reviewCount);
        return this;
    }

}
