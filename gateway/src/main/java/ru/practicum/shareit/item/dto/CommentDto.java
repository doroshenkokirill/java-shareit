package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validations.Create;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    @NotBlank(groups = {Create.class})
    @Size(max = 512, groups = {Create.class})
    private String text;
}
