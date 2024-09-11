package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validations.StartBeforeEndDateValid;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@StartBeforeEndDateValid
public class BookingDtoRequest {
    private int itemId;

    private LocalDateTime start;

    private LocalDateTime end;
}
