package edu.polina.kafkaservice.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "app.service")
public class AppProperties {
    private Duration cacheTtl;
    private int rateLimit;
    private Duration window;
    private Duration keySpaceTtl;
    private boolean registrationEnabled;
    private boolean testMode;
}
