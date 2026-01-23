package edu.polina.kafkaservice.kafka;

import edu.polina.kafkaservice.domain.EventType;
import edu.polina.kafkaservice.infrastructure.database.EventAudit;
import edu.polina.kafkaservice.infrastructure.database.EventAuditRepository;
import edu.polina.kafkaservice.infrastructure.metric.CustomMetricService;
import edu.polina.kafkaservice.properties.KafkaProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserDeletedConsumer {

    private final CustomMetricService customMetricService;
    private final KafkaProperties props;
    private final EventAuditRepository repository;

    @KafkaListener(topics = "${app.kafka.user-deleted-topic}", containerFactory = "kafkaListenerContainerFactory")
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
            customMetricService.processed(props.getUserDeletedTopic());
            log.info("Событие обработано, eventId = {}, eventType = {}", event.getEventId(), "DELETED");
        }
    }

}
