package edu.polina.kafkaservice.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "app.kafka")
public class KafkaProperties {
    String userCreatedTopic;
    String userDeletedTopic;
    String userCreatedDlqTopic;
}
