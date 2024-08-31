package ru.practicum.shareit.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserDto;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return userClient.getAll();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getById(@PathVariable int userId) {
        return userClient.getById(userId);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody @Valid UserDto userDto) {
        return userClient.add(userDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteById(@PathVariable int userId) {
        return userClient.deleteById(userId);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@RequestBody UserDto newUserDto,
                                         @PathVariable int userId) {
        return userClient.update(userId, newUserDto);
    }
}
