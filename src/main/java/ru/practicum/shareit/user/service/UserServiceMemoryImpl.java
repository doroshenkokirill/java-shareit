package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.NotUniqueEmailException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceMemoryImpl implements UserService {

    private int id = 1;
    private UserMapper userMapper;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public UserDto add(UserDto userDto) {
        validateUserDtoEmail(userDto);
        User user =  UserMapper.toUser(userDto);
        user.setId(id++);
        users.put(user.getId(),user);
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto update(int id, UserDto userDto) {
        validateById(id);
        validateUserDtoEmail(userDto);
        if (userDto.getEmail() != null) {
            users.get(id).setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null) {
            users.get(id).setName(userDto.getName());
        }
        return UserMapper.toUserDto(users.get(id));
    }

    @Override
    public void deleteById(int id) {
        validateById(id);
        users.remove(id);
    }

    @Override
    public List<UserDto> getAll() {
         return users.values().stream().map(UserMapper::toUserDto).toList();
    }

    @Override
    public UserDto getById(int id) {
        validateById(id);
        return UserMapper.toUserDto(users.get(id));
    }

    @Override
    public void validateById(int id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException(String.format("User c id - %d не найден. Ошибка", id));
        }
    }

    @Override
    public void validateUserDtoEmail(UserDto userDto) {
        if (users.values().stream().anyMatch(user -> user.getEmail().equals(userDto.getEmail()))) {
            throw new NotUniqueEmailException(String.format("Этот email - %s не уникальный. Ошибка", userDto.getEmail()));
        }
    }
}
