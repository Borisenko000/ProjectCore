package edu.polina.kafkaservice.kafka;

import edu.polina.kafkaservice.infrastructure.database.EventAudit;
import edu.polina.kafkaservice.infrastructure.database.EventAuditRepository;
import edu.polina.kafkaservice.infrastructure.metric.CustomMetricService;
import edu.polina.kafkaservice.properties.KafkaProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

import static edu.polina.kafkaservice.domain.EventType.CREATED;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserCreatedConsumer {

    private final EventAuditRepository repository;
    private final CustomMetricService customMetricService;
    private final KafkaProperties props;

    @KafkaListener(topics = "${app.kafka.user-created-topic}", containerFactory = "kafkaListenerContainerFactory")
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
            customMetricService.processed(props.getUserCreatedTopic());
            log.info("Событие обработано, eventId = {}, eventType = {}", event.getEventId(), "CREATED");
        }
    }


}
