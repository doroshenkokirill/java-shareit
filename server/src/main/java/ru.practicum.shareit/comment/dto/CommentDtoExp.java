package ru.practicum.shareit.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class CommentDtoExp {
    private Integer id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
