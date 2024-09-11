package ru.practicum.shareit.user.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.NotUniqueEmailException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class UserServiceImplTest {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1).name("Name").email("123@email.ru").build();
        user = userRepository.save(user);
    }

    @Test
    void add() throws Exception {
        UserDto newUserDto = UserDto.builder()
                .name("UserDto Name")
                .email("1234@email.ru")
                .build();

        UserDto savedUserDto = userService.add(newUserDto);

        assertThat(savedUserDto.getId()).isNotNull();
        assertThat(savedUserDto.getName()).isEqualTo(newUserDto.getName());

        User savedUser = userRepository.findById(savedUserDto.getId()).orElseThrow();
        assertThat(savedUser.getName()).isEqualTo(newUserDto.getName());
    }

    @Test
    void addWithError() throws NotUniqueEmailException {
        UserDto newUserDto = UserDto.builder().name("UserDto Name").email(user.getEmail()).build();

        assertThrows(NotUniqueEmailException.class, () -> userService.add(newUserDto));
    }


    @Test
    void update() throws Exception {
        UserDto updatedUserDto = UserDto.builder()
                .name("UpdatedUserDto")
                .email("UpdatedUserDto@email.ru")
                .build();

        UserDto userDtoToSave = userService.update(user.getId(), updatedUserDto);

        assertThat(userDtoToSave.getName()).isEqualTo(user.getName());
        assertThat(userDtoToSave.getEmail()).isEqualTo(user.getEmail());

        User updatedUser = userRepository.findById(user.getId()).orElseThrow();
        assertThat(updatedUser.getName()).isEqualTo(updatedUserDto.getName());
        assertThat(updatedUser.getEmail()).isEqualTo(updatedUserDto.getEmail());
    }

    @Test
    void deleteById() throws Exception {
        userService.deleteById(user.getId());

        assertThat(userRepository.existsById(user.getId())).isFalse();
    }

    @Test
    void getById() throws Exception {
        UserDto userDto = userService.getById(user.getId());

        assertThat(userDto).isNotNull();
        assertThat(userDto.getId()).isEqualTo(user.getId());
        assertThat(userDto.getName()).isEqualTo(user.getName());
        assertThat(userDto.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void getByIdWithError() throws NotFoundException {
        assertThrows(NotFoundException.class, () -> userService.getById(user.getId() + 1));
    }

    @Test
    void getAll() throws Exception {
        List<UserDto> userDtoList = userService.getAll();

        assertEquals(1, userDtoList.size());
        assertThat(userDtoList.getFirst().getName()).isEqualTo(user.getName());
        assertThat(userDtoList.getFirst().getEmail()).isEqualTo(user.getEmail());
    }
}
