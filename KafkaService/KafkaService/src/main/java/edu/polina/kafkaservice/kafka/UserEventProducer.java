package edu.polina.kafkaservice.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
public class UserEventProducer {

    private KafkaTemplate<Long, Object> kafkaTemplate;

    public UserEventProducer(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Value("${user-created-topic}")
    public String topicUserCreatedName;

    @Value("${user-deleted-topic}")
    public String topicUserDeletedName;

    public void sendEventUserCreated(Long userId) {
        UserCreatedEvent event = UserCreatedEvent.newBuilder()
                .setEventId(UUID.randomUUID().toString())
                .setUserId(userId)
                .setOccurredAt(Instant.now())
                .build();
        kafkaTemplate.send(topicUserCreatedName, userId, event);
        log.info("Событие eventId = {} отправлено в topic = {}", event.getEventId(), topicUserCreatedName);
    }

    public void sendEventUserDeleted(Long userId) {
        UserDeletedEvent event = UserDeletedEvent.newBuilder()
                .setEventId(UUID.randomUUID().toString())
                .setUserId(userId)
                .setOccurredAt(Instant.now())
                .build();
        kafkaTemplate.send(topicUserDeletedName, userId, event);
    }
}
