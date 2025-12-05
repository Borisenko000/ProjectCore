package edu.spring.service;
import edu.spring.module.User;
import edu.spring.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class UserServiceImpl implements UserService{

    private final UserRepository repository;

    @Override
    public void createUser(String login, String password) {
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        repository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByLog(String login) {
        return repository.getUserByLogin(login);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        return repository.getUserById(id);
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
