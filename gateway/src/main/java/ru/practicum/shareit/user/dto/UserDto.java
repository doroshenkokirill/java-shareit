package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validations.Create;

@Data
@Builder
@AllArgsConstructor
public class UserDto {
    @NotNull
    private int id;
    @NotEmpty(groups = {Create.class})
    @Size(max = 255, groups = {Create.class})
    private String name;
    @Email(groups = {Create.class})
    @NotEmpty(groups = {Create.class})
    private String email;
}
