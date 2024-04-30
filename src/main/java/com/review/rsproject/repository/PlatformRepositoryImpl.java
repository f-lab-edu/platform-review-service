package com.review.rsproject.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.review.rsproject.domain.Platform;
import com.review.rsproject.type.PlatformSort;
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

        return new PageImpl<>(platforms, pageable, platformCount);
    }

    @Override
    public Page<Platform> findByQuery(String platformName, Pageable pageable, PlatformSort sort) {
        List<Platform> platforms = query.select(platform).from(platform)
                .where(platform.status.eq(PlatformStatus.ACCEPT).and(platform.name.contains(platformName)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(sortConverter(sort))
                .fetch();

        Long platformCount = query.select(platform.count()).from(platform)
                .where(platform.name.contains(platformName))
                .fetchFirst();

        return new PageImpl<>(platforms, pageable, platformCount);
    }



    private OrderSpecifier<?> sortConverter(PlatformSort sort) {

        switch (sort) {
            case STAR_ASC ->  { return new OrderSpecifier(Order.ASC, platform.star); }
            case STAR_DESC ->  { return new OrderSpecifier(Order.DESC, platform.star); }


            case DATE_DESC -> { return new OrderSpecifier(Order.DESC, platform.createdDt); }
            default ->  { return new OrderSpecifier(Order.ASC, platform.createdDt); }
        }

    }

}
