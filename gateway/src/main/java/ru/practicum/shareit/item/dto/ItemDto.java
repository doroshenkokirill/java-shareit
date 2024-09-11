package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ItemDto {
    private int id;
    @NotBlank
    @Size(max = 255)
    private String name;
    @NotBlank
    @Size(max = 512)
    private String description;
    @NotNull
    private Boolean available;
    private Integer requestId;
    private List<CommentDto> comments;
    private BookingDtoRequest lastBooking;
    private BookingDtoRequest nextBooking;
}
