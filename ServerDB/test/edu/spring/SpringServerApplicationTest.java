package edu.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.spring.module.User;
import edu.spring.module.UserRequestDto;
import edu.spring.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class SpringServerApplicationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    private MockMvc mockMvc;


    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Test
    public void createUserAndSaveInDb() throws Exception {
        UserRequestDto request = new UserRequestDto("DaryaEmail", "darya87", "darya87");
        String requestToJSON = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/api/v1/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestToJSON))
                .andExpect(status().isOk());
        User result = userRepository.getUserByEmail("DaryaEmail").orElseThrow(() -> new EntityNotFoundException("User not found"));
        assertThat(result.getEmail()).isEqualTo("DaryaEmail");
        assertThat(result.getPassword()).isNotEqualTo("darya87");
    }

    @Test
    public void createUserAndCheckHashInDb() throws Exception {
        String password = "123456";
        UserRequestDto request = new UserRequestDto("DaryaEmail", "123456", "123456");
        String requestToJSON = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/api/v1/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestToJSON))
                .andExpect(status().isOk());
        User savedUser = userRepository.getUserByEmail("DaryaEmail")
                .orElseThrow(() -> new EntityNotFoundException("TEST: User not found"));
        assertThat(savedUser.getPassword()).isNotEqualTo(password);
        assertThat(encoder.matches(password, savedUser.getPassword())).isTrue();
    }
}
