package com.review.rsproject.repository;

import com.review.rsproject.domain.Platform;
import com.review.rsproject.type.PlatformStatus;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlatformRepository extends JpaRepository<Platform, Long>, CustomPlatformRepository {

    @EntityGraph(attributePaths = {"member"})
    @Query("select p from Platform p where p.id = :id")
    Optional<Platform> findByIdAndFetchMember(@Param("id") Long id);
}
