package edu.spring.controller;

import edu.spring.module.User;
import edu.spring.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping("/{login}")
    public User getUser(@PathVariable String login) {
        Optional<User> user =  service.getUserByLog(login);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("User with login" + login + "doesn't exist");
        }
        return user.get();
    }
    @GetMapping
    public List<User> getAll() {
        return service.getAllUsers();
    }

    @PostMapping("/{login}/{password}")
    public void postUser(@PathVariable @Valid String login, @PathVariable @Valid String password) {
        service.createUser(login, password);
    }

    @DeleteMapping("/{login}")
    public void deleteUser(@PathVariable String login) {
        service.deleteUser(getUser(login));
    }
}
