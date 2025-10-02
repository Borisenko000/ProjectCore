package edu.polina.kafkaservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic userCreatedTopic(@Value("${user-created-topic}") String name) {
        return TopicBuilder.name(name).partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic userDeletedTopic(@Value("${user-deleted-topic}") String name) {
        return TopicBuilder.name(name).partitions(1).replicas(1).build();
    }
}
