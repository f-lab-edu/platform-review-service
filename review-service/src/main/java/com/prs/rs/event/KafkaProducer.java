package com.prs.rs.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prs.rs.dto.request.PlatformRefreshDto;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void platformRefresh(String topic, PlatformRefreshDto platformRefreshDto) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";
        try {
            jsonInString = mapper.writeValueAsString(platformRefreshDto);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic,
            jsonInString);

        // Producer 예외 처리
        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Unable to send message=[{}] due to : {}", platformRefreshDto.toString(),
                    ex.getMessage());
            }
        });



    }
}