package com.review.rsproject.repository;

import com.review.rsproject.domain.Platform;
import com.review.rsproject.type.SortType;
import com.review.rsproject.type.PlatformStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPlatformRepository {

    Page<Platform> findByStatus(Pageable pageable, PlatformStatus status);
    
    Page<Platform> findByQuery(String platformName, Pageable pageable, SortType sort);

}
