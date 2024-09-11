package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validations.Create;
import ru.practicum.shareit.validations.Update;

@Data
@Builder
@AllArgsConstructor
public class ItemResponseDto {
    private int id;
    @NotBlank(groups = Create.class)
    @Size(max = 255, groups = Create.class)
    private String name;
    @NotBlank(groups = {Create.class, Update.class})
    @Size(max = 512,groups = Create.class)
    private String description;
    @NotNull(groups = {Create.class, Update.class})
    private Boolean available;
    @NotNull(groups = {Update.class})
    private Integer requestId;
}
