package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ItemRequestController.class)
public class ItemRequestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemRequestService itemRequestService;
    @Autowired
    private ObjectMapper objectMapper;
    private ItemRequestDto itemRequestDto;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @BeforeEach
    void setUp() {
        itemRequestDto = ItemRequestDto.builder().id(1).description("Description").build();
    }


    @Test
    void getAll() throws Exception {
        List<ItemRequestDto> itemRequestDtoList = List.of(itemRequestDto);
        when(itemRequestService.getAll(eq(1))).thenReturn(itemRequestDtoList);
        mockMvc.perform(get("/requests/all")
                        .header(USER_ID_HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(itemRequestDtoList.size()))
                .andExpect(jsonPath("$[0].id").value(itemRequestDto.getId()))
                .andExpect(jsonPath("$[0].description").value(itemRequestDto.getDescription()));

        verify(itemRequestService, times(1)).getAll(eq(1));

    }

    @Test
    void getByUserIdAndReqId() throws Exception {
        when(itemRequestService.getByUserIdAndReqId(eq(1), eq(1))).thenReturn(itemRequestDto);
        mockMvc.perform(get("/requests/{requestId}", 1)
                        .header(USER_ID_HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemRequestDto.getId()))
                .andExpect(jsonPath("$.description").value(itemRequestDto.getDescription()));
        verify(itemRequestService, times(1)).getByUserIdAndReqId(eq(1), eq(1));
    }

    @Test
    void getByUserId() throws Exception {
        List<ItemRequestDto> itemRequestDtoList = List.of(itemRequestDto);
        when(itemRequestService.getByUserId(eq(1))).thenReturn(itemRequestDtoList);
        mockMvc.perform(get("/requests")
                        .header(USER_ID_HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(itemRequestDtoList.size()))
                .andExpect(jsonPath("$[0].id").value(itemRequestDto.getId()))
                .andExpect(jsonPath("$[0].description").value(itemRequestDto.getDescription()));
        verify(itemRequestService, times(1)).getByUserId(eq(1));
    }

    @Test
    void add() throws Exception {
        when(itemRequestService.add(any(ItemRequestDto.class), eq(1))).thenReturn(itemRequestDto);
        mockMvc.perform(post("/requests")
                        .header(USER_ID_HEADER, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemRequestDto.getId()))
                .andExpect(jsonPath("$.description").value(itemRequestDto.getDescription()));
        verify(itemRequestService, times(1)).add(any(ItemRequestDto.class), eq(1));
    }
}