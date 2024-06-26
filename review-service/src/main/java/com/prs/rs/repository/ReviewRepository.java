package com.prs.rs.repository;

import com.prs.rs.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ReviewRepository extends JpaRepository<Review, Long> {


    @Query("select r from Review r where r.platformId = :platformId")
    Page<Review> findByIdFromPlatform(@Param("platformId") Long platformId, Pageable pageable);


}
