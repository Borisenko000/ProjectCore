package edu.polina.kafkaservice.infrastructure.metric;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class CustomMetricService {

    private final MeterRegistry meterRegistry;

    public CustomMetricService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void processed(String topic) {
        Counter.builder("kafka_processed_events_total")
                .description("Счетчик обработаных событий kafka")
                .tag("topic", topic)
                .register(meterRegistry)
                .increment();
    }

    public void dlq(String topic) {
        Counter.builder("kafka_dlq_events_total")
                .description("Счетчик обработанных dlq событий kafka")
                .register(meterRegistry)
                .increment();
    }

    public void rateLimited(HttpServletResponse response) {
        Counter.builder("http_server_requests_rate_limit")
                .description("Количество отклонений rate limit")
                .tag("status", String.valueOf(response.getStatus()))
                .register(meterRegistry)
                .increment();
    }
}
