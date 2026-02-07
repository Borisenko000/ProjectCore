package edu.spring.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.spring.module.User;
import edu.spring.module.UserRequestDto;
import edu.spring.module.UserResponseDto;
import edu.spring.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository repository;
    private final BCryptPasswordEncoder encoder;

    @Override
    public UserResponseDto createUser(UserRequestDto request) {
        var passwordHash = encoder.encode(request.password());
        User user = User.builder()
                .email(request.email())
                .password(passwordHash)
                .build();
        repository.save(user);
        UserResponseDto responseDto = new UserResponseDto(user.getId(), user.getEmail());
        log.info("User with id = {} was created", user.getId());
        return responseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return repository
                .getUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found. Email: %s".formatted(email)));
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return repository.getUserById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found. id: %d".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public void deleteUser(User user) {
        repository.deleteUserById(user.getId());
    }
}
