package edu.polina.kafkaservice.keyspace;

import edu.polina.kafkaservice.domain.UserStatus;
import edu.polina.kafkaservice.infrastructure.User;
import edu.polina.kafkaservice.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeySpaceNotificationsProcessor {

    private final String PREFIX = "activation:";

    private final UserRepository repository;

    public void handleKeySpaceNotification(String key) {
        Long userId = Long.parseLong(key.substring(PREFIX.length()));
        Optional<User> userOptional = repository.findById(userId);
        if (userOptional.isEmpty()) {
            log.info("ACTIVATION CHECK: User with id = {} not found", userId);
            return;
        }
        User user = userOptional.get();
        user.setStatus(UserStatus.EXPIRED);
        repository.save(user);
        log.info("ACTIVATION CHECK: User's key activation has expired, id = {}, status = {}", userId, user.status);
    }

}
