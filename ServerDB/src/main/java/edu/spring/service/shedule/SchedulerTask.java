package edu.spring.service.shedule;

import edu.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class SchedulerTask {

    private final BCryptPasswordEncoder encoder;
    private final UserRepository repository;
    private AtomicLong userId = new AtomicLong(1);

    @Autowired
    public SchedulerTask(BCryptPasswordEncoder encoder, UserRepository repository) {
        this.encoder = encoder;
        this.repository = repository;
    }
/*
    @Scheduled(fixedRate = 2000)
    public void migrateColumnWithPassword() {
        Optional<String> plainPasswordOptional = repository.findPlainPasswordById(userId.longValue());
        if (plainPasswordOptional.isPresent()) {
            String plainPassword = plainPasswordOptional.get();
            String hashPassword = encoder.encode(plainPassword);
            Optional<String> hashPasswordOptional = repository.findHashPasswordById(userId.longValue());
            if (hashPasswordOptional.isEmpty()) {
                repository.updateHashPassword(userId.longValue(), hashPassword);
            }
            userId.incrementAndGet();
            return;
        }
        userId.incrementAndGet();
    }

 */
}
