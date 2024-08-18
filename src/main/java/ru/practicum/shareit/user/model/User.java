package ru.practicum.shareit.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
@AllArgsConstructor
public class User {
    private int id;
    private String name;
    @Email
    @NotEmpty
    private String email;
}
