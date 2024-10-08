package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentDtoExp;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static ru.practicum.shareit.Constants.USER_ID_HEADER;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public ItemDto getById(@PathVariable int itemId, @RequestHeader(USER_ID_HEADER) int userId) {
        return itemService.getById(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader(USER_ID_HEADER) int owner) {
        return itemService.getAll(owner);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam("text") String text) {
        return itemService.search(text);
    }

    @PostMapping
    public ItemDto add(@RequestBody ItemDto itemDto,
                       @RequestHeader(USER_ID_HEADER) int ownerId) {
        return itemService.add(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody ItemDto itemDto, @PathVariable int itemId,
                          @RequestHeader(USER_ID_HEADER) int owner) {
        return itemService.update(itemId, owner, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoExp addComment(@PathVariable int itemId,
                                    @RequestHeader(USER_ID_HEADER) int userId,
                                    @RequestBody CommentDto comment) {
        return itemService.addComment(itemId, userId, comment);
    }
}
