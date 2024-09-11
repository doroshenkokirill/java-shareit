package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validations.Create;
import ru.practicum.shareit.validations.Update;

@Data
@Builder
@AllArgsConstructor
public class UserDto {
    private int id;
    @NotBlank(groups = {Create.class})
    @Size(max = 255, groups = {Create.class, Update.class})
    private String name;
    @Email(groups = {Create.class, Update.class})
    @Size(max = 255, groups = {Create.class, Update.class})
    @NotEmpty(groups = {Create.class})
    private String email;
}
