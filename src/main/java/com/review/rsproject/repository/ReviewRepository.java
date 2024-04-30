package com.review.rsproject.repository;

import com.review.rsproject.domain.Review;
import jakarta.persistence.TypedQuery;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select count(r), sum(r.star) from Review r where r.platform.id = :platformId")
    List<Long[]> findByStar(@Param("platformId") Long platformId);

    @Query("select r from Review r where r.id = :id")
    @EntityGraph(attributePaths = {"member"})
    Optional<Review> findByIdFetchMember(@Param("id") Long id);

}
