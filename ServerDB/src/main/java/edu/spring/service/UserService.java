package edu.spring.service;

import edu.spring.module.User;
import edu.spring.module.UserRequestDto;
import edu.spring.module.UserResponseDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public UserResponseDto createUser(UserRequestDto request);

    public User getUserByEmail(String email);

    public User getUserById(Long id);

    public void deleteUser(User user);

    public List<User> getAllUsers();
}
