package ru.practicum.shareit.item.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.WrongOwnerException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.service.UserService;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private int id = 1;
    private final Map<Integer, Item> items = new HashMap<>();
    private ItemMapper itemMapper;
    private final UserService userService;

    @Override
    public ItemDto add(ItemDto itemDto, int owner) {
        userService.validateById(owner);
        Item item = ItemMapper.toItem(itemDto);
        item.setId(id++);
        item.setOwner(owner);
        items.put(item.getId(),item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto update(int itemId, int owner, ItemDto itemDto) {
        validateById(itemId);
        Item someItem = items.get(itemId);

        if (someItem.getOwner() != owner) {
            throw new WrongOwnerException("Только владелец может вносить изменения");
        }
        if (itemDto.getName() != null) someItem.setName(itemDto.getName());
        if (itemDto.getDescription() != null) someItem.setDescription(itemDto.getDescription());
        if (itemDto.getAvailable() != null) someItem.setAvailable(itemDto.getAvailable());

        return ItemMapper.toItemDto(someItem);
    }

    @Override
    public List<ItemDto> getAll(int owner) {
        return items.values().stream().filter(item -> item.getOwner() == owner).map(ItemMapper::toItemDto).toList();
    }

    @Override
    public ItemDto getById(int id) {
        validateById(id);
        return ItemMapper.toItemDto(items.get(id));
    }

    @Override
    public void validateById(int id) {
        if (!items.containsKey(id)) {
            throw new NotFoundException(String.format("Item c id - %d не найден. Ошибка", id));
        }
    }

    @Override
    public List<ItemDto> search(String text) {
        if (Objects.equals(text, "")) return Collections.emptyList();

        return items.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> item.getName() != null && item.getName().toLowerCase().contains(text.toLowerCase()) ||
                        item.getDescription() != null && item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .map(ItemMapper::toItemDto)
                .toList();
    }
}
