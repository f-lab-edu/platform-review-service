package com.review.rsproject.repository;

import com.review.rsproject.domain.Review;
import com.review.rsproject.dto.response.ReviewCountDto;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select new com.review.rsproject.dto.response.ReviewCountDto(count(r), sum(r.star)) from Review r where r.platform.id = :platformId")
    ReviewCountDto findByStar(@Param("platformId") Long platformId);

    @Query("select r from Review r where r.id = :id")
    @EntityGraph(attributePaths = {"member", "platform"})
    Optional<Review> findByIdFetchOther(@Param("id") Long id);


    @Query("select r from Review r where r.platform.id = :platformId")
    @EntityGraph(attributePaths = {"member"})
    Page<Review> findByIdFromPlatform(@Param("platformId") Long platformId, Pageable pageable);


}
