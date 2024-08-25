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
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @GetMapping("/{bookingId}")
    public BookingDto get(@PathVariable int bookingId, @RequestHeader(USER_ID_HEADER) int userId) {
        return bookingService.get(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getAll(@RequestHeader(USER_ID_HEADER) int userId,
                                   @RequestParam(name = "state", defaultValue = "ALL") BookingState state) {
        return bookingService.getAll(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllByOwner(@RequestHeader(USER_ID_HEADER) int userId,
                                          @RequestParam(name = "state", defaultValue = "ALL") BookingState state) {
        return bookingService.getAllByOwner(userId, state);
    }

    @PostMapping
    public BookingDto create(@RequestBody @Valid BookingDtoRequest bookingDtoRequest,
                             @RequestHeader(USER_ID_HEADER) int bookerId) {
        return bookingService.create(bookingDtoRequest, bookerId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approve(@PathVariable int bookingId,
                              @RequestParam boolean approved,
                              @RequestHeader(USER_ID_HEADER) int userId) {
        return bookingService.approve(bookingId, approved, userId);
    }
}
