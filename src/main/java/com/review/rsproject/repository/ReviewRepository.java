package com.review.rsproject.repository;

import com.review.rsproject.domain.Review;
import jakarta.persistence.TypedQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select count(r), sum(r.star) from Review r where r.platform.id = :platformId")
    List<Long[]> findByStar(@Param("platformId") Long platformId);

}
