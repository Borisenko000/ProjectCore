package edu.spring.service;

import edu.spring.module.User;
import edu.spring.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Test
    void saveUserWithCorrectLoginAndPassword() {
        userService.createUser("TestUser", "555");
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User user =captor.getValue();
        assertEquals("TestUser", user.getLogin());
    }

    @Test
    void deleteUserWithCorrectLoginAndPassword() {
        User user = new User("TestUser", "555");
        userService.deleteUser(user);
        verify(userRepository).deleteUserById(user.getId());
    }


}
