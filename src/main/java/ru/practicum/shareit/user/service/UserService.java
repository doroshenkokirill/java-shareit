package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto add(UserDto userDto);

    UserDto update(int id, UserDto userDto);

    void deleteById(int id);

    List<UserDto> getAll();

    UserDto getById(int id);

    void validateById(int id);

    void validateUserDtoEmail(UserDto userDto);
}
