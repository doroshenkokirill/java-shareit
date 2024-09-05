package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(BookingController.class)
class BookingControllerTest {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    @MockBean
    private BookingService bookingService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    private BookingDtoRequest bookingDtoRequest;
    private BookingDto bookingDtoResponse;
    private User booker;
    private User owner;
    private Item item;

    @Test
    void getTest() throws Exception {
        int bookingId = 1;
        int userId = 1;

        Mockito.when(bookingService.get(eq(bookingId), eq(userId)))
                .thenReturn(bookingDtoResponse);

        mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header(USER_ID_HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDtoResponse.getId()))
                .andExpect(jsonPath("$.start").value(bookingDtoResponse.getStart().format(date)))
                .andExpect(jsonPath("$.end").value(bookingDtoResponse.getEnd().format(date)))
                .andExpect(jsonPath("$.status").value(bookingDtoResponse.getStatus().name()))
                .andExpect(jsonPath("$.item.id").value(item.getId()))
                .andExpect(jsonPath("$.booker.id").value(booker.getId()));
    }

    @Test
    void getAllTest() throws Exception {
        int userId = 1;
        BookingState state = BookingState.ALL;
        List<BookingDto> bookings = List.of(bookingDtoResponse);

        Mockito.when(bookingService.getAll(eq(userId), eq(state)))
                .thenReturn(bookings);

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", state.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(bookings.size()))
                .andExpect(jsonPath("$[0].id").value(bookingDtoResponse.getId()))
                .andExpect(jsonPath("$[0].status").value(bookingDtoResponse.getStatus().name()));
    }

    @Test
    void getAllByOwnerTest() throws Exception {
        List<BookingDto> bookings = List.of(bookingDtoResponse);

        Mockito.when(bookingService.getAllByOwner(anyInt(), eq(BookingState.ALL)))
                .thenReturn(bookings);

        mockMvc.perform(get("/bookings/owner")
                        .header(USER_ID_HEADER, 1)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(bookings.size()))
                .andExpect(jsonPath("$[0].id").value(bookingDtoResponse.getId()))
                .andExpect(jsonPath("$[0].start").value(bookingDtoResponse.getStart().format(date)))
                .andExpect(jsonPath("$[0].end").value(bookingDtoResponse.getEnd().format(date)))
                .andExpect(jsonPath("$[0].status").value(bookingDtoResponse.getStatus().name()));
    }

    @Test
    void createTest() throws Exception {
        int userId = 1;

        Mockito.when(bookingService.create(any(BookingDtoRequest.class), anyInt()))
                .thenReturn(bookingDtoResponse);

        mockMvc.perform(post("/bookings")
                        .header(USER_ID_HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDtoRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDtoResponse.getId()))
                .andExpect(jsonPath("$.start").value(bookingDtoResponse.getStart().format(date)))
                .andExpect(jsonPath("$.end").value(bookingDtoResponse.getEnd().format(date)))
                .andExpect(jsonPath("$.status").value(bookingDtoResponse.getStatus().name()));
    }

    @Test
    void approveTest() throws Exception {
        bookingDtoResponse.setStatus(BookingStatus.APPROVED);
        boolean approved = true;
        int bookingId = 1;
        int userId = 1;

        Mockito.when(bookingService.approve(eq(bookingId), eq(approved), eq(userId)))
                .thenReturn(bookingDtoResponse);

        mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .header(USER_ID_HEADER, userId)
                        .param("approved", String.valueOf(approved))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDtoResponse.getId()))
                .andExpect(jsonPath("$.start").value(bookingDtoResponse.getStart().format(date)))
                .andExpect(jsonPath("$.end").value(bookingDtoResponse.getEnd().format(date)))
                .andExpect(jsonPath("$.status").value(bookingDtoResponse.getStatus().name()));
    }

    @BeforeEach
    void setup() {
        booker = User.builder().id(1).name("Booker Name").email("booker@gmail.com").build();
        owner = User.builder().id(1).name("Owner Name").email("owner@gmail.com").build();
        item = Item.builder().id(1).name("Item Name").description("Item description").available(true).owner(owner).build();

        bookingDtoRequest = BookingDtoRequest.builder()
                .itemId(1)
                .start(LocalDateTime.now().plusHours(5))
                .end(LocalDateTime.now().plusHours(5))
                .build();

        bookingDtoResponse = BookingDto.builder()
                .id(1)
                .start(LocalDateTime.parse("2020-10-20T00:00:00"))
                .end(LocalDateTime.parse("2020-10-20T01:00:00"))
                .status(BookingStatus.WAITING)
                .item(item)
                .booker(booker)
                .build();
    }
}