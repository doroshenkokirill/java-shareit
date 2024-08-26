package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingService {
    BookingDto get(int bookingId, int userId);

    BookingDto create(BookingDtoRequest bookingDtoRequest, int bookerId);

    BookingDto approve(int bookingId, boolean approved, int userId);

    List<BookingDto> getAll(int userId, BookingState bookingState);

    List<BookingDto> getAllByOwner(int userId, BookingState bookingState);
}
