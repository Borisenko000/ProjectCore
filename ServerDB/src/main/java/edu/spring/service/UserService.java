package edu.spring.service;

import edu.spring.module.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public void createUser(String login, String password);

    public Optional<User> getUserByLog(String login);

    public Optional<User> getUserById(Long id);

    public void deleteUser(User user);

    public List<User> getAllUsers();
}
