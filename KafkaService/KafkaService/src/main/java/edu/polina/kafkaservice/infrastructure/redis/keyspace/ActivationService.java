package edu.polina.kafkaservice.infrastructure.redis.keyspace;

import edu.polina.kafkaservice.properties.AppProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ActivationService {

    private final StringRedisTemplate redisTemplate;

    private final String PREFIX = "activation:";

    private final AppProperties props;

    public void createActivationKey(Long userId) {
        String key = PREFIX + userId.toString();
        redisTemplate.opsForValue().set(key, "pending", props.getKeySpaceTtl());
        log.info("ACTIVATION CHECK: User with id = {} is pending for activation", userId);
    }
}
