package edu.spring.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ConditionalOnProperty(name = "scheduler.enabled", matchIfMissing = true)
@EnableScheduling
public class SchedulerConfig {
}
