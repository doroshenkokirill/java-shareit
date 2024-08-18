package ru.practicum.shareit.booking.model;

import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class Booking {
    private int id;
    private LocalDateTime start;
    private LocalDateTime end;
    private int item;
    private int booker;
    private BookingStatus status;
}
