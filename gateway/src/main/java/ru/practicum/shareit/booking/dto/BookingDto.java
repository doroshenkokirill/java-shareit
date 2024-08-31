package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class BookingDto {
    private int id;
    private LocalDateTime start;
    private LocalDateTime end;
    private int itemId;
    private int bookerId;
    private BookingStatus status;
}
