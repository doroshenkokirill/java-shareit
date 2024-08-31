package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @GetMapping("/{bookingId}")
    public BookingDto get(@PathVariable int bookingId,
                          @RequestHeader(USER_ID_HEADER) int userId) {
        return bookingService.get(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getAll(@RequestHeader(USER_ID_HEADER) int userId,
                                   @RequestParam(name = "state", defaultValue = "ALL") BookingState bookingState) {
        return bookingService.getAll(userId, bookingState);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllByOwner(@RequestHeader(USER_ID_HEADER) int userId,
                                          @RequestParam(name = "state", defaultValue = "ALL") BookingState bookingState) {
        return bookingService.getAllByOwner(userId, bookingState);
    }

    @PostMapping
    public BookingDto create(@RequestBody BookingDtoRequest bookingDtoRequest,
                             @RequestHeader(USER_ID_HEADER) int bookerId) {
        return bookingService.create(bookingDtoRequest, bookerId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approve(@RequestHeader(USER_ID_HEADER) int userId,
                              @PathVariable int bookingId,
                              @RequestParam boolean approved) {
        return bookingService.approve(bookingId, approved, userId);
    }
}
