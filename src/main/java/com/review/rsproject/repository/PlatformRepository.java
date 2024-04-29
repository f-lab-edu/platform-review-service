package com.review.rsproject.repository;

import com.review.rsproject.domain.Platform;
import com.review.rsproject.type.PlatformStatus;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlatformRepository extends JpaRepository<Platform, Long>, CustomPlatformRepository {


}
