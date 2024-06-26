package com.prs.ps.event;

import static com.prs.ps.common.ConstantValues.PLATFORM_REFRESH_TOPIC;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prs.ps.domain.Platform;
import com.prs.ps.dto.response.PlatformRefreshDto;
import com.prs.ps.service.PlatformServiceWrapper;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlatformRefresher {

    private final PlatformServiceWrapper platformService;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String CACHE_PREFIX = "platform_refresh:";

    @KafkaListener(topics = PLATFORM_REFRESH_TOPIC)
    public void refreshPlatform(String message) throws JsonProcessingException {

        // message -> json
        PlatformRefreshDto platformRefreshDto = objectMapper.readValue(message,
            PlatformRefreshDto.class);

        String cacheKey = CACHE_PREFIX + platformRefreshDto.getMessageId();

        // 이미 처리된 경우 return
        if (Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey))) {
            return;
        }


        // update
        platformService.refreshPlatformScore(platformRefreshDto.getPlatformId(),
            Platform.getEmpty(), platformRefreshDto);

        redisTemplate.opsForValue().set(cacheKey, "processed");
        redisTemplate.expire(cacheKey, Duration.ofMinutes(1));

    }


}
