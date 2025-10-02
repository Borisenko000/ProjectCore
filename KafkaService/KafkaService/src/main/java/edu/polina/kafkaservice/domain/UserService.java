package edu.polina.kafkaservice.domain;

import edu.polina.kafkaservice.api.UserRequestDto;
import edu.polina.kafkaservice.infrastructure.User;
import edu.polina.kafkaservice.infrastructure.UserRepository;
import edu.polina.kafkaservice.kafka.UserEventProducer;
import edu.polina.kafkaservice.keyspace.ActivationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@AllArgsConstructor
@Service
@Slf4j
public class UserService {

    private final UserEventProducer kafkaUserEventProducer;
    private final UserRepository userRepository;
    private final ActivationService activationService;


    public User createUser(UserRequestDto request) {
        User user = User.builder()
                .name(request.name())
                .password(request.password())
                .status(UserStatus.NOT_ACTIVE)
                .build();
        userRepository.save(user);
        kafkaUserEventProducer.sendEventUserCreated(user.getId());
        activationService.createActivationKey(user.getId());
        return user;
    }

    @CacheEvict(value = "user", key = "#id")
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
        kafkaUserEventProducer.sendEventUserDeleted(id);
    }

    @CacheEvict(value = "user", key = "#id")
    public User updateUser(Long id, UserRequestDto request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User (id = %s) не найден".formatted(id)
                ));
        user.setName(request.name());
        user.setPassword(request.password());
        User updated = userRepository.save(user);
        return updated;
    }

    @Cacheable(value = "user", key = "#id")
    public User getUser(Long id) {
        log.info("DB HIT: загружаю user = {} из базы", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User (id = %s) не найден".formatted(id)
                ));
    }

}
