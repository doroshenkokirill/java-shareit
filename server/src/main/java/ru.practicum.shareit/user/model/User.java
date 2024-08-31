package ru.practicum.shareit.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty
    private String name;
    @Email
    @NotEmpty
    private String email;
}
