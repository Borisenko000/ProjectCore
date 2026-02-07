package edu.spring.service;

import edu.spring.module.User;
import edu.spring.module.UserRequestDto;
import edu.spring.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder encoder;

    @Test
    void saveUserWithCorrectLoginAndPassword() {
        UserRequestDto requestDto = new UserRequestDto("TestUserEmail", "123456", "123456");
        userService.createUser(requestDto);
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User comparableUser =captor.getValue();
        assertEquals("TestUserEmail", comparableUser.getEmail());
    }

    @Test
    void deleteUserWithCorrectLoginAndPassword() {
        User user = new User("TestUserEmail", "123456");
        user.setId(1L);
        userService.deleteUser(user);
        verify(userRepository).deleteUserById(1L);
    }

    @Test
    void SignUpHashingTest() {
        String password = "123456";
        String fakeHashPassword = "$73dhr$fdjnk";
        when(encoder.encode(password)).thenReturn(fakeHashPassword);
        UserRequestDto requestDto = new UserRequestDto("TestUserEmail", password, password);
        userService.createUser(requestDto);
        verify(encoder).encode(password);
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User savedUser = captor.getValue();
        assertEquals("TestUserEmail", savedUser.getEmail());
        assertEquals(fakeHashPassword, savedUser.getPassword());
    }


}
