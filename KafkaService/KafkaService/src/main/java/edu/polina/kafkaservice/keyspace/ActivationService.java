package edu.polina.kafkaservice.keyspace;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActivationService {

    private final StringRedisTemplate redisTemplate;

    private final String PREFIX = "activation:";

    @Value("${key-space-notifications.ttl}")
    private Duration ttl;

    public void createActivationKey(Long userId) {
        String key = PREFIX + userId.toString();
        redisTemplate.opsForValue().set(key, "pending", ttl);
        log.info("ACTIVATION CHECK: User with id = {} is pending for activation", userId);
    }
}
