package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder().id(1).name("Name").build();
    }

    @Test
    void getAll() throws Exception {
        List<UserDto> userDtoList = new ArrayList<>();
        userDtoList.add(userDto);
        when(userService.getAll()).thenReturn(userDtoList);
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(userDto.getId()))
                .andExpect(jsonPath("$[0].name").value(userDto.getName()));
        verify(userService, times(1)).getAll();
    }

    @Test
    void getById() throws Exception {
        when(userService.getById(eq(1))).thenReturn(userDto);
        mockMvc.perform(get("/users/{userId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.name").value(userDto.getName()));
        verify(userService, times(1)).getById(eq(1));
    }

    @Test
    void add() throws Exception {
        when(userService.add(any(UserDto.class))).thenReturn(userDto);
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.name").value(userDto.getName()));
        verify(userService, times(1)).add(any(UserDto.class));

    }

    @Test
    void deleteById() throws Exception {
        mockMvc.perform(delete("/users/{userId}", 1))
                .andExpect(status().isOk());
        verify(userService, times(1)).deleteById(eq(1));
    }

    @Test
    void update() throws Exception {
        UserDto newUserDto = UserDto.builder().name("New name").build();
        when(userService.update(eq(1), any(UserDto.class))).thenReturn(newUserDto);
        mockMvc.perform(patch("/users/{userId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(newUserDto.getId()))
                .andExpect(jsonPath("$.name").value(newUserDto.getName()));
        verify(userService, times(1)).update(eq(1), any(UserDto.class));
    }
}