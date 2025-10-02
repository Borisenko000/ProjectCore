package edu.polina.kafkaservice.kafka;

import edu.polina.kafkaservice.domain.EventType;
import edu.polina.kafkaservice.infrastructure.EventAudit;
import edu.polina.kafkaservice.infrastructure.EventAuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDeletedConsumer {

    private final EventAuditRepository repository;

    @KafkaListener(topics = "${user-deleted-topic}", containerFactory = "kafkaListenerContainerFactory")
    public void listen(UserDeletedEvent event) {
        var isEventExist = repository.findByEventId(event.getEventId().toString());
        if (isEventExist.isEmpty()) {
            EventAudit eventAudit = EventAudit.builder()
                    .eventId(event.getEventId().toString())
                    .eventType(EventType.DELETED)
                    .userId(event.getUserId())
                    .occurredAt(Timestamp.from(event.getOccurredAt()))
                    .build();
            repository.save(eventAudit);
            log.info("Событие обработано, eventId = {}, eventType = {}", event.getEventId(), "DELETED");

        }
    }

}
