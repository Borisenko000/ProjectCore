package edu.spring.repository;

import edu.spring.module.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    void GetUserIfExist() {
        User user = new User("PolinaTest", "123456");
        userRepository.save(user);
        User result = userRepository.getUserByLogin("PolinaTest").get();
        assertThat(result.getLogin()).isEqualTo("PolinaTest");
        assertThat(result.getPassword()).isEqualTo("123456");
    }
}
