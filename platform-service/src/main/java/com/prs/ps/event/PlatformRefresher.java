package com.prs.ps.event;


import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PlatformRefresher {

    @KafkaListener(topics = "platform-refresh-topic")
    public void refreshPlatform(String platformId) {
        log.info("Kafka Message: ->" + platformId);

    }
}
