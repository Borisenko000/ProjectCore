package edu.polina.kafkaservice.api;

import edu.polina.kafkaservice.infrastructure.User;
import edu.polina.kafkaservice.domain.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public User createUser(@RequestBody UserRequestDto request) { return userService.createUser(request); }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody UserRequestDto request) { return userService.updateUser(id, request); }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) { return userService.getUser(id); }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) { userService.deleteUser(id); } }