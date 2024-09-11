package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validations.Create;
import ru.practicum.shareit.validations.Update;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class CommentDto {
    @NotBlank(groups = {Create.class, Update.class})
    @Size(max = 512, groups = {Create.class, Update.class})
    private String text;
    private LocalDateTime created;
}
