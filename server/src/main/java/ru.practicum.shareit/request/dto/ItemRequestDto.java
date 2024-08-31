package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@Builder
@AllArgsConstructor
public class ItemRequestDto {
    private int id;
    private String description;
    private User requester;
    private LocalDateTime created;
    private List<ItemDto> items;
}
