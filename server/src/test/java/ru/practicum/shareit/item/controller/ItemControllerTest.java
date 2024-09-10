package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentDtoExp;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.Constants.USER_ID_HEADER;

@AutoConfigureMockMvc
@WebMvcTest(ItemController.class)
class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemService itemService;
    @Autowired
    private ObjectMapper objectMapper;
    private ItemDto itemDto;
    private CommentDto commentDto;
    private CommentDtoExp commentDtoExp;

    @BeforeEach
    void setUp() {
        itemDto = ItemDto.builder().id(1).name("Item").available(true).description("Item description").build();
        commentDto = CommentDto.builder().id(1).text("Some comment").created(LocalDateTime.now()).build();
        commentDtoExp = CommentDtoExp.builder().id(1).text("Some commentExp").created(LocalDateTime.now()).build();
    }

    @Test
    void getById() throws Exception {
        when(itemService.getById(eq(1), eq(1)))
                .thenReturn(itemDto);

        mockMvc.perform(get("/items/{itemId}", 1)
                        .header(USER_ID_HEADER, 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()))
                .andExpect(jsonPath("$.name").value(itemDto.getName()))
                .andExpect(jsonPath("$.available").value(itemDto.getAvailable()))
                .andExpect(jsonPath("$.description").value(itemDto.getDescription()));
    }

    @Test
    void getAll() throws Exception {
        List<ItemDto> itemDtoList = new ArrayList<>();
        itemDtoList.add(itemDto);
        when(itemService.getAll(eq(1))).thenReturn(itemDtoList);
        mockMvc.perform(get("/items")
                        .header(USER_ID_HEADER, 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(itemDtoList.size()));
    }

    @Test
    void search() throws Exception {
        List<ItemDto> itemDtoList = new ArrayList<>();
        itemDtoList.add(itemDto);
        when(itemService.search(anyString())).thenReturn(itemDtoList);
        mockMvc.perform(get("/items/search")
                        .param("text", "Item")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(itemDtoList.size()));
    }

    @Test
    void add() throws Exception {
        when(itemService.add(any(ItemDto.class), anyInt())).thenReturn(itemDto);
        mockMvc.perform(post("/items")
                        .header(USER_ID_HEADER, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()))
                .andExpect(jsonPath("$.name").value(itemDto.getName()))
                .andExpect(jsonPath("$.available").value(itemDto.getAvailable()))
                .andExpect(jsonPath("$.description").value(itemDto.getDescription()));
    }

    @Test
    void update() throws Exception {
        ItemDto newItem = ItemDto.builder().name("New name").build();
        when(itemService.update(eq(1), eq(1), any(ItemDto.class))).thenReturn(itemDto);
        mockMvc.perform(patch("/items/{itemId}", 1)
                        .header(USER_ID_HEADER, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(itemDto.getName()));
    }

    @Test
    void addComment() throws Exception {
        when(itemService.addComment(eq(1), eq(1), any(CommentDto.class))).thenReturn(commentDtoExp);
        mockMvc.perform(post("/items/{itemId}/comment", 1)
                        .header(USER_ID_HEADER, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentDtoExp.getId()));
    }
}