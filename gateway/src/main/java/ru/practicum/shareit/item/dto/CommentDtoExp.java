package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class CommentDtoExp {
    private Integer id;
    @NotBlank
    private String text;
    private String authorName;
    private LocalDateTime created;
}
