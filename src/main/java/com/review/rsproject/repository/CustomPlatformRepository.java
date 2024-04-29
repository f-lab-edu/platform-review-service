package com.review.rsproject.repository;

import com.review.rsproject.domain.Platform;
import com.review.rsproject.type.PlatformStatus;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomPlatformRepository {

    Page<Platform> findByStatus(Pageable pageable, PlatformStatus status);

}
