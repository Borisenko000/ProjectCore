package edu.polina.kafkaservice.kafka;

import edu.polina.kafkaservice.domain.EventType;
import edu.polina.kafkaservice.infrastructure.EventAudit;
import edu.polina.kafkaservice.infrastructure.EventAuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

import static edu.polina.kafkaservice.domain.EventType.CREATED;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserCreatedConsumer {

    private final EventAuditRepository repository;

    @KafkaListener(topics = "${user-created-topic}", containerFactory = "kafkaListenerContainerFactory")
    public void listen(UserCreatedEvent event) {
        var isEventExist = repository.findByEventId(event.getEventId().toString());
        if (isEventExist.isEmpty()) {
            if (event.getUserId() == 13L) {
                log.info("Событие отправлено в dlq topic, eventId = {}", event.getEventId());
                throw new IllegalArgumentException("id не должно быть равно 13");
            }
            EventAudit eventAudit = EventAudit.builder()
                    .eventId(event.getEventId().toString())
                    .eventType(CREATED)
                    .userId(event.getUserId())
                    .occurredAt(Timestamp.from(event.getOccurredAt()))
                    .build();
            repository.save(eventAudit);
            log.info("Событие обработано, eventId = {}, eventType = {}", event.getEventId(), "CREATED");
        }
    }


}
