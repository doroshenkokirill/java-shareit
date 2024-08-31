package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/items")
public class ItemController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemClient itemClient;

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(@PathVariable int itemId,
                                          @RequestHeader(USER_ID_HEADER) int userId) {
        return itemClient.getById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader(USER_ID_HEADER) int ownerId) {
        return itemClient.getAll(ownerId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestHeader(USER_ID_HEADER) int userId,
                                         @RequestParam("text") String text) {
        return itemClient.search(text, userId);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody @Valid ItemDto itemDto,
                                      @RequestHeader(USER_ID_HEADER) int ownerId) {
        return itemClient.add(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestBody ItemDto newItemDto,
                                         @PathVariable int itemId,
                                         @RequestHeader(USER_ID_HEADER) int ownerId) {
        return itemClient.update(itemId, ownerId, newItemDto);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@PathVariable int itemId,
                                             @RequestHeader(USER_ID_HEADER) int userId,
                                             @Valid @RequestBody CommentDto comment) {
        return itemClient.addComment(itemId, userId, comment);
    }
}
