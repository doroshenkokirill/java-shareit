package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService requestService;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @GetMapping("/all")
    public List<ItemRequestDto> getAll(@RequestHeader(USER_ID_HEADER) int userId) {
        return requestService.getAll(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getByUserIdAndReqId(@RequestHeader(USER_ID_HEADER) int userId,
                                              @PathVariable int requestId) {
        return requestService.getByUserIdAndReqId(userId, requestId);
    }

    @GetMapping
    public List<ItemRequestDto> getByUserId(@RequestHeader(USER_ID_HEADER) int userId) {
        return requestService.getByUserId(userId);
    }

    @PostMapping
    public ItemRequestDto add(@RequestBody ItemRequestDto requestDto,
                              @RequestHeader(USER_ID_HEADER) int userId) {
        return requestService.add(requestDto, userId);
    }
}
