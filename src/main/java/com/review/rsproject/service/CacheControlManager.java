package com.review.rsproject.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CacheControlManager {
    private final RedisTemplate<String, Object> redisTemplate;


    public void evictCacheByPattern(String pattern) {

        RedisConnectionFactory connectionFactory = redisTemplate.getConnectionFactory();
        RedisConnection connection = connectionFactory.getConnection();


        ScanOptions options = ScanOptions.scanOptions().match(pattern + "*").count(10).build();

        Cursor<byte[]> cursor = connection.scan(options);

        while (cursor.hasNext()) {
            String key = new String(cursor.next());
            redisTemplate.delete(key);

        }

    }

}
