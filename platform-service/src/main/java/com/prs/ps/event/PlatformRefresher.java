package com.prs.ps.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prs.ps.domain.Platform;
import com.prs.ps.dto.response.PlatformRefreshDto;
import com.prs.ps.exception.PlatformNotFoundException;
import com.prs.ps.repository.PlatformRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static com.prs.ps.common.ConstantValues.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlatformRefresher {
    
    private final PlatformRepository platformRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = PLATFORM_REFRESH_TOPIC)
    @Transactional
    public void refreshPlatform(String message) throws JsonProcessingException {

        // message -> json
        PlatformRefreshDto platformRefreshDto = objectMapper.readValue(message, PlatformRefreshDto.class);


        Optional<Platform> findPlatform = platformRepository.findById(platformRefreshDto.getPlatformId());

        Platform platform = findPlatform.orElseThrow(PlatformNotFoundException::new);

        // update
        Platform updatedPlatform = platform.updateStar(platformRefreshDto.getReviewCount(), platformRefreshDto.getReviewTotalStar());

        platformRepository.save(updatedPlatform);

    }
}
