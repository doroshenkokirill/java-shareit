package ru.practicum.shareit.request.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemRequestClient itemRequestClient;

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@RequestHeader(USER_ID_HEADER) int userId) {
        return itemRequestClient.getAll(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getByUserId(@RequestHeader(USER_ID_HEADER) int userId) {
        return itemRequestClient.getByUserId(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getByUserIdAndReqId(@RequestHeader(USER_ID_HEADER) int userId,
                                                      @PathVariable int requestId) {
        return itemRequestClient.getByUserIdAndReqId(userId, requestId);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody @Valid ItemRequestDto itemRequestDto,
                                      @RequestHeader(USER_ID_HEADER) int userId) {
        return itemRequestClient.add(itemRequestDto, userId);
    }
}
