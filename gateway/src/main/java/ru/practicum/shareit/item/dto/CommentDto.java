package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class CommentDto {
    private int id;
    @NotBlank
    private String text;
    private int itemId;
    private String author;
    private LocalDateTime created;
}
