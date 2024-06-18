package com.prs.ps.repository;

import com.prs.ps.domain.Platform;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface PlatformRepository extends JpaRepository<Platform, Long>,
    CustomPlatformRepository {

    @EntityGraph(attributePaths = {"member"})
    @Query("select p from Platform p where p.id = :id")
    Optional<Platform> findByIdAndFetchMember(@Param("id") Long id);
}
