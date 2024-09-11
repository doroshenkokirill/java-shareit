package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<UserDto> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable int userId) {
        return userService.getById(userId);
    }

    @PostMapping
    public UserDto add(@RequestBody UserDto userDto) {
        return userService.add(userDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteById(@PathVariable int userId) {
        userService.deleteById(userId);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@RequestBody UserDto newUserDto,
                          @PathVariable int userId) {
        return userService.update(userId, newUserDto);
    }
}