package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.validations.Create;

import static ru.practicum.shareit.Constants.USER_ID_HEADER;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
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
    public ResponseEntity<Object> add(@RequestBody @Validated(Create.class) ItemRequestDto itemRequestDto,
                                      @RequestHeader(USER_ID_HEADER) int userId) {
        return itemRequestClient.add(itemRequestDto, userId);
    }
}
