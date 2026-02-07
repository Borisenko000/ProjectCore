package edu.spring.controller;

import edu.spring.module.UserRequestDto;
import edu.spring.module.User;
import edu.spring.module.UserResponseDto;
import edu.spring.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }


    @GetMapping("/{login}")
    public User getUser(@PathVariable String email) {
        User user =  service.getUserByEmail(email);
        return user;
    }
    @GetMapping
    public List<User> getAll() {
        return service.getAllUsers();
    }

    @PostMapping("/signup")
    public UserResponseDto postUser(@RequestBody @Valid UserRequestDto request) {
        UserResponseDto responseDto = service.createUser(request);
        return responseDto;
    }

    @DeleteMapping("/{email}")
    public void deleteUser(@PathVariable String email) {
        service.deleteUser(getUser(email));
    }
}
