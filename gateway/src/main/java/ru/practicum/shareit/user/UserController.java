package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserClient userClient;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable("id") long id) {
        return userClient.getUser(id);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Valid User user) {
        log.info("creating user with email: {}", user.getEmail());
        return userClient.createUser(user);
    }

    @PatchMapping("{id}")
    public ResponseEntity<Object> updateUser(@PathVariable("id") long id,
                                             @RequestBody @Valid User user) {
        log.info("updating user with id: {}", id);
        return userClient.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") long id) {
        log.info("deleting user with id: {}", id);
        userClient.deleteUser(id);
    }
}
