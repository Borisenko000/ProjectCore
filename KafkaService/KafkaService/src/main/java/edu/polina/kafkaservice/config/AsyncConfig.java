package edu.polina.kafkaservice.config;

import edu.polina.kafkaservice.infrastructure.multithreading.ContextCopyingDecorator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Slf4j
@Configuration
public class AsyncConfig {

    @Bean(name = "contextTransferTaskExecutor")
    public Executor taskExecutor() {
        int cores = Runtime.getRuntime().availableProcessors();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(cores + 1);
        executor.setMaxPoolSize(cores * 2);
        executor.setKeepAliveSeconds(15);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("AsyncContext-");
        executor.setTaskDecorator(new ContextCopyingDecorator());
        executor.initialize();
        return executor;
    }
}
