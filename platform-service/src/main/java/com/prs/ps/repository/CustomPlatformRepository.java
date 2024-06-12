package com.prs.ps.repository;

import com.prs.ps.domain.Platform;
import com.prs.ps.type.PlatformStatus;
import com.prs.ps.type.SortType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPlatformRepository {

    Page<Platform> findByStatus(Pageable pageable, PlatformStatus status);
    
    Page<Platform> findByQuery(String platformName, Pageable pageable, SortType sort);

}
