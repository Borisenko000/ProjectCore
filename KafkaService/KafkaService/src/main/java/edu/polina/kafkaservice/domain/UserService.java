package edu.polina.kafkaservice.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.polina.kafkaservice.api.UserRequestDto;
import edu.polina.kafkaservice.api.WelcomeRequestDto;
import edu.polina.kafkaservice.api.WelcomeResponseDto;
import edu.polina.kafkaservice.properties.AppProperties;
import edu.polina.kafkaservice.external.WireMockWelcomeClient;
import edu.polina.kafkaservice.infrastructure.database.User;
import edu.polina.kafkaservice.infrastructure.database.UserRepository;
import edu.polina.kafkaservice.kafka.UserEventProducer;
import edu.polina.kafkaservice.infrastructure.redis.keyspace.ActivationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserEventProducer kafkaUserEventProducer;
    private final UserRepository userRepository;
    private final ActivationService activationService;
    private final WireMockWelcomeClient welcomeClient;
    private final ObjectMapper objectMapper;
    private final AppProperties props;
    private final AuditService auditService;

    public User createUser(UserRequestDto request) {
        User user = User.builder()
                .name(request.name())
                .password(request.password())
                .status(UserStatus.NOT_ACTIVE)
                .build();
        log.info("CHEСK MDC: User with name = {} is creating", user.getName());
        userRepository.save(user);
        if (props.isRegistrationEnabled()) {
            WelcomeRequestDto welcomeRequestDto = new WelcomeRequestDto(request.name());
            try {
                WelcomeResponseDto responseDto = welcomeClient.createWelcomeResponse(welcomeRequestDto);
                log.info("WIREMOCK - OK: {} ", responseDto.message());

            } catch (HttpServerErrorException e) {
                try {
                    var responseDto = objectMapper.readValue(e.getResponseBodyAsString(), WelcomeResponseDto.class);
                    log.warn("WIREMOCK - WARN: {}", responseDto.message());
                } catch (JsonProcessingException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        kafkaUserEventProducer.sendEventUserCreated(user.getId());
        //activationService.createActivationKey(user.getId());
        auditService.logUserCreated(user);

        return user;
    }

    @Transactional
    @CacheEvict(value = "user", key = "#id")
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
        kafkaUserEventProducer.sendEventUserDeleted(id);
    }

    @Transactional
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

    public User getUserWithTestMode(Long id) {
        if (props.isTestMode()) {
            log.info("CLOUD BUS CHECK: ON");
        }
        else {
            log.info("CLOUD BUS CHECK: OFF");
        }
        log.info("DB HIT: загружаю user = {} из базы", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User (id = %s) не найден".formatted(id)
                ));
    }

}
