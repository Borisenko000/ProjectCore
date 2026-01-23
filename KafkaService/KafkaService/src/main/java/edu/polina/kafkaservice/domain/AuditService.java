package edu.polina.kafkaservice.domain;

import edu.polina.kafkaservice.infrastructure.database.User;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Data
@Slf4j
@Service
public class AuditService {

    @Async("contextTransferTaskExecutor")
    public void logUserCreated(User user) {
        log.info("CHEСK MDC: User with name = {} has been created", user.getName());
    }
}
