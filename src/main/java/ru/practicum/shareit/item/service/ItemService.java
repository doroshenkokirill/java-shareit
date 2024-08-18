package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto add(ItemDto itemDto, int owner);

    ItemDto update(int itemId, int owner, ItemDto itemDto);

    List<ItemDto> getAll(int owner);

    ItemDto getById(int id);

    List<ItemDto> search(String text);

    void validateById(int id);
}
