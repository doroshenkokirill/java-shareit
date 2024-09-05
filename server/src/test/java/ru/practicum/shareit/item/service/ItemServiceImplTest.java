package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    @InjectMocks
    private ItemServiceImpl itemService;
    @Mock
    private ItemRepository itemRepository;

    @Test
    void update() {
        User user = User.builder()
                .id(1).name("Name").email("123@mail.ru")
                .build();
        Item item = Item.builder()
                .id(1).owner(user).name("Item Name").description("Description").available(true)
                .build();

        ItemDto itemDtoNew = ItemDto.builder()
                .id(1).name("New User Name").description("New Description")
                .build();

        Item updatedItem = new Item();
        updatedItem.setId(1);
        updatedItem.setName(itemDtoNew.getName());
        updatedItem.setDescription(itemDtoNew.getDescription());

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(itemRepository.save(Mockito.any(Item.class))).thenReturn(updatedItem);

        ItemDto itemDto = itemService.update(item.getId(), user.getId(), itemDtoNew);

        assertEquals(itemDto.getName(), itemDtoNew.getName());

        Item resultItem = itemRepository.findById(item.getId()).orElseThrow();
        assertEquals(resultItem.getName(), itemDtoNew.getName());
    }

    @Test
    void getAll() {
    }

    @Test
    void getByIdTest() {
    }

    @Test
    void search() {
    }

    @Test
    void addComment() {
    }
}