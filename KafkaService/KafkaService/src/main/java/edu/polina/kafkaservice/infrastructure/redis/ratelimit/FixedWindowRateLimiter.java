package edu.polina.kafkaservice.infrastructure.redis.ratelimit;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@AllArgsConstructor
@Slf4j
public class FixedWindowRateLimiter {

    private final StringRedisTemplate stringRedisTemplate;

    public boolean allowRequest(
            String userId,
            int limit,
            Duration windowSize) {

        long windowIndex = System.currentTimeMillis() / windowSize.toMillis();
        String key = String.format("rate:%s:%s", userId, windowIndex);

        Long countHints = stringRedisTemplate.opsForValue()
                .increment(key);
       // log.info("key = {}, countHits = {}", key, countHints);
        if (countHints != null && countHints == 1L) {
            stringRedisTemplate.expire(key, windowSize);
        }
        return countHints <= limit;
    }

}
