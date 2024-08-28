package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.dto.CommentDtoExp;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto add(ItemDto itemDto, int owner);

    ItemDto update(int itemId, int owner, ItemDto itemDto);

    List<ItemDto> getAll(int owner);


    List<ItemDto> search(String text);

    ItemDto getById(int id, int userId);

    void validateById(int id);

    CommentDtoExp addComment(int itemId, int userId, Comment comment);
}
