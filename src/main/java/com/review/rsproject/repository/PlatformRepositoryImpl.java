package com.review.rsproject.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.review.rsproject.domain.Platform;
import com.review.rsproject.type.PlatformStatus;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.List;

import static com.review.rsproject.domain.QPlatform.platform;

public class PlatformRepositoryImpl implements CustomPlatformRepository{

    private final JPAQueryFactory query;

    @Autowired
    public PlatformRepositoryImpl(EntityManager entityManager) {
        this.query = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Platform> findByStatus(Pageable pageable, PlatformStatus status) {
        List<Platform> platforms = query.select(platform).from(platform)
                .where((status != null) ? platform.status.eq(status) : null)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        Long platformCount = query.select(platform.count()).from(platform).fetchFirst();

        return new PageImpl<Platform>(platforms, pageable, platformCount);
    }
}
