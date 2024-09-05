package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

@Data
@Builder
@AllArgsConstructor
public class UserDto {
    private int id;
    private String name;
    private String email;
}
