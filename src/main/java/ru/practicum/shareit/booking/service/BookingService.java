package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingService {
    BookingDto create(BookingDtoRequest bookingDtoRequest, int bookerId);

    BookingDto get(int bookingId, int userId);

    List<BookingDto> getAll(int userId, BookingState state);

    List<BookingDto> getAllByOwner(int userId, BookingState state);

    BookingDto approve(int bookingId, boolean approved, int userId);
}
