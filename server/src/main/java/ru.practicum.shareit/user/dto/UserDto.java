package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserDto {
    private int id;
    @NotNull
    @Size(max = 255)
    private String name;
    @Size(max = 512)
    private String email;
}
