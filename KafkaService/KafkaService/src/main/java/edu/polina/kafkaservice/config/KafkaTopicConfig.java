package edu.polina.kafkaservice.config;

import edu.polina.kafkaservice.properties.AppProperties;
import edu.polina.kafkaservice.properties.KafkaProperties;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@RequiredArgsConstructor
@Configuration
public class KafkaTopicConfig {

    private final KafkaProperties props;

    @Bean
    public NewTopic userCreatedTopic() {
        return TopicBuilder.name(props.getUserCreatedTopic()).partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic userDeletedTopic() {
        return TopicBuilder.name(props.getUserDeletedTopic()).partitions(1).replicas(1).build();
    }
}
