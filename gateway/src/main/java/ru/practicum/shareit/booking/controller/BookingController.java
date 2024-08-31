package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingState;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final BookingClient bookingClient;

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> get(@PathVariable int bookingId,
                                      @RequestHeader(USER_ID_HEADER) int userId) {
        return bookingClient.get(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader(USER_ID_HEADER) int userId,
                                         @RequestParam(name = "state", defaultValue = "ALL") BookingState state) {
        return bookingClient.getAll(userId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllByOwner(@RequestHeader(USER_ID_HEADER) int userId,
                                                @RequestParam(name = "state", defaultValue = "ALL") BookingState state) {
        return bookingClient.getAllByOwner(userId, state);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid BookingDtoRequest bookingDtoRequest,
                                         @RequestHeader(USER_ID_HEADER) int bookerId) {
        return bookingClient.create(bookingDtoRequest, bookerId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approve(@RequestHeader(USER_ID_HEADER) int userId,
                                          @PathVariable int bookingId,
                                          @RequestParam boolean approved) {
        return bookingClient.approve(userId, bookingId, approved);
    }
}
