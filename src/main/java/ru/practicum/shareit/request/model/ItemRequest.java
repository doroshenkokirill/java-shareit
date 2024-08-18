package ru.practicum.shareit.request.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
public class ItemRequest {
    private int id;
    private String description;
    private Integer requestor;
    private LocalDateTime created;
}
