package edu.polina.kafkaservice.kafka;

import edu.polina.kafkaservice.properties.KafkaProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserEventProducer {

    private final KafkaTemplate<Long, Object> kafkaTemplate;
    private final KafkaProperties props;

    public void sendEventUserCreated(Long userId) {
        String topicUserCreatedName = props.getUserCreatedTopic();
        UserCreatedEvent event = UserCreatedEvent.newBuilder()
                .setEventId(UUID.randomUUID().toString())
                .setUserId(userId)
                .setOccurredAt(Instant.now())
                .build();
        kafkaTemplate.send(topicUserCreatedName, userId, event);
        log.info("Событие eventId = {} отправлено в topic = {}", event.getEventId(), topicUserCreatedName);
    }

    public void sendEventUserDeleted(Long userId) {
        String topicUserDeletedName = props.getUserDeletedTopic();
        UserDeletedEvent event = UserDeletedEvent.newBuilder()
                .setEventId(UUID.randomUUID().toString())
                .setUserId(userId)
                .setOccurredAt(Instant.now())
                .build();
        kafkaTemplate.send(topicUserDeletedName, userId, event);
    }
}
