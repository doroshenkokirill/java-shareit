package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.NotUniqueEmailException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto add(UserDto userDto) {
        validateUserDtoEmail(userDto);
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDto)));
    }

    @Override
    public UserDto update(int id, UserDto userDto) {
        validateById(id);
        User oldUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User c id - %d не найден. Ошибка", id)));
        String newEmail = userDto.getEmail();
        if (newEmail != null && !newEmail.equals(oldUser.getEmail())) {
            validateUserDtoEmail(userDto);
            oldUser.setEmail(newEmail);
        }
        String newName = userDto.getName();
        if (newName != null) oldUser.setName(newName);
        User updatedUser = userRepository.save(oldUser);
        return UserMapper.toUserDto(updatedUser);
    }


    @Override
    public void deleteById(int id) {
        validateById(id);
        userRepository.deleteById(id);
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream().map(UserMapper::toUserDto).toList();
    }

    @Override
    public UserDto getById(int id) {
        validateById(id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User c id - %d не найден. Ошибка", id)));
        return UserMapper.toUserDto(user);
    }


    @Override
    public void validateById(int id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException(String.format("User c id - %d не найден. Ошибка", id));
        }
    }

    @Override
    public void validateUserDtoEmail(UserDto userDto) {
        if (userRepository.findAll().stream().anyMatch(user -> user.getEmail().equals(userDto.getEmail()))) {
            throw new NotUniqueEmailException(String.format("Этот email - %s не уникальный. Ошибка", userDto.getEmail()));
        }
    }
}
