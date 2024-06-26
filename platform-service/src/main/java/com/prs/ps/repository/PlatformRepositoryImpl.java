package com.prs.ps.repository;

import static com.prs.ps.domain.QPlatform.*;

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
        List<Platform> platforms = query.select(platform).from(platform)
            .where((status != null) ? platform.status.eq(status) : null)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long platformCount = query.select(platform.count()).from(platform)
            .fetchFirst();

        return new PageImpl<>(platforms, pageable, platformCount);
    }

    @Override
    public Page<Platform> findByQuery(String platformName, Pageable pageable, SortType sort) {
        List<Platform> platforms = query.select(platform).from(platform)
            .where(platform.status.eq(PlatformStatus.ACCEPT)
                .and(platform.name.contains(platformName)))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(sortConverter(sort))
            .fetch();

        Long platformCount = query.select(platform.count()).from(platform)
            .where(platform.name.contains(platformName))
            .fetchFirst();

        return new PageImpl<>(platforms, pageable, platformCount);
    }


    private OrderSpecifier<?> sortConverter(SortType sort) {

        switch (sort) {
            case STAR_ASC -> {
                return new OrderSpecifier(Order.ASC, platform.avgScore);
            }
            case STAR_DESC -> {
                return new OrderSpecifier(Order.DESC, platform.avgScore);
            }

            case DATE_DESC -> {
                return new OrderSpecifier(Order.DESC, platform.createdDt);
            }
            default -> {
                return new OrderSpecifier(Order.ASC, platform.createdDt);
            }
        }

    }

}
