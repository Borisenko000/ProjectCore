package edu.polina.kafkaservice.kafka;

import edu.polina.kafkaservice.infrastructure.metric.CustomMetricService;
import edu.polina.kafkaservice.properties.KafkaProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Component
@Slf4j
public class KafkaDLQConsumer {

    private final CustomMetricService customMetricService;
    private final KafkaProperties props;

    @KafkaListener(topics = "${app.kafka.user-created-dlq-topic}", groupId = "user-service-dlq-viewer")
    public void listenDLQ(ConsumerRecord<Long, Object> record) {
        log.error("key = " + record.key());
        for(Header header : record.headers()) {
            log.error(headerToString(header));
        }
        customMetricService.dlq(props.getUserCreatedDlqTopic());
    }

    private static String headerToString(Header header) {
        byte[] value = header.value();
        if (header.key().endsWith("original-partition") && value.length == 4) {
            int partition = ByteBuffer.wrap(value).getInt();
            return header.key() + "=" + partition;
        }
        if ((header.key().endsWith("original-offset") || header.key().endsWith("original-timestamp")) && value.length == 8) {
            long num = ByteBuffer.wrap(value).getLong();
            return header.key() + "=" + num;
        }

        // Остальное как текст
        return header.key() + "=" + new String(value, StandardCharsets.UTF_8);
    }
}
