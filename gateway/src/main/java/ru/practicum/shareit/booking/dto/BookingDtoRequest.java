package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validations.StartBeforeEndDateValid;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class BookingDtoRequest {
    private int itemId;

    @FutureOrPresent
    @NotNull
    @StartBeforeEndDateValid
    private LocalDateTime start;

    @Future
    @NotNull
    @StartBeforeEndDateValid
    private LocalDateTime end;
}
