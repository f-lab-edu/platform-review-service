package com.prs.ps.repository;

import com.prs.ps.domain.Platform;
import com.prs.ps.domain.QPlatform;
import com.prs.ps.type.PlatformStatus;
import com.prs.ps.type.SortType;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class PlatformRepositoryImpl implements CustomPlatformRepository {

    private final JPAQueryFactory query;

    @Autowired
    public PlatformRepositoryImpl(EntityManager entityManager) {
        this.query = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Platform> findByStatus(Pageable pageable, PlatformStatus status) {
        List<Platform> platforms = query.select(QPlatform.platform).from(QPlatform.platform)
            .where((status != null) ? QPlatform.platform.status.eq(status) : null)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long platformCount = query.select(QPlatform.platform.count()).from(QPlatform.platform)
            .fetchFirst();

        return new PageImpl<>(platforms, pageable, platformCount);
    }

    @Override
    public Page<Platform> findByQuery(String platformName, Pageable pageable, SortType sort) {
        List<Platform> platforms = query.select(QPlatform.platform).from(QPlatform.platform)
            .where(QPlatform.platform.status.eq(PlatformStatus.ACCEPT)
                .and(QPlatform.platform.name.contains(platformName)))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(sortConverter(sort))
            .fetch();

        Long platformCount = query.select(QPlatform.platform.count()).from(QPlatform.platform)
            .where(QPlatform.platform.name.contains(platformName))
            .fetchFirst();

        return new PageImpl<>(platforms, pageable, platformCount);
    }


    private OrderSpecifier<?> sortConverter(SortType sort) {

        switch (sort) {
            case STAR_ASC -> {
                return new OrderSpecifier(Order.ASC, QPlatform.platform.star);
            }
            case STAR_DESC -> {
                return new OrderSpecifier(Order.DESC, QPlatform.platform.star);
            }

            case DATE_DESC -> {
                return new OrderSpecifier(Order.DESC, QPlatform.platform.createdDt);
            }
            default -> {
                return new OrderSpecifier(Order.ASC, QPlatform.platform.createdDt);
            }
        }

    }

}
