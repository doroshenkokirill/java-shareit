package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.validations.Create;
import ru.practicum.shareit.validations.Update;

import java.util.Collections;

import static ru.practicum.shareit.Constants.USER_ID_HEADER;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/items")
public class ItemController {
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
        if (text.isBlank()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return itemClient.search(text, userId);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody @Validated({Create.class}) ItemDtoRequest itemDto,
                                      @RequestHeader(USER_ID_HEADER) int ownerId) {
        return itemClient.add(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestBody @Validated({Update.class}) ItemDtoRequest newItemDto,
                                         @PathVariable int itemId,
                                         @RequestHeader(USER_ID_HEADER) int ownerId) {
        return itemClient.update(itemId, ownerId, newItemDto);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@PathVariable int itemId,
                                             @RequestHeader(USER_ID_HEADER) int userId,
                                             @Validated({Create.class}) @RequestBody CommentDto comment) {
        return itemClient.addComment(itemId, userId, comment);
    }
}
