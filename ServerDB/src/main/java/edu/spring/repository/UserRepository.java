package edu.spring.repository;

import edu.spring.module.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> getUserById(Long id);

    public Optional<User> getUserByLogin(String login);

    public void deleteUserById(Long id);


}
