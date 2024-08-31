package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.AddCommentException;
import ru.practicum.shareit.exceptions.BookingException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private BookingServiceImpl bookingService;

    private User owner;
    private Item item;
    private User booker;
    private Booking booking;
    private BookingDto bookingDto;
    private BookingDtoRequest bookingDtoRequest;

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Test
    void create() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(booker));
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingDto createdBooking = bookingService.create(bookingDtoRequest, booker.getId());

        assertAll(
                () -> assertNotNull(createdBooking),
                () -> assertEquals(bookingDto.getId(), createdBooking.getId()),
                () -> assertEquals(booking.getStart().format(dateTimeFormatter), createdBooking.getStart().format(dateTimeFormatter)),
                () -> assertEquals(booking.getEnd().format(dateTimeFormatter), createdBooking.getEnd().format(dateTimeFormatter)),
                () -> assertEquals(booking.getStatus(), createdBooking.getStatus(), "Статус не совпадает")
        );

        item.setAvailable(false);
        assertThrows(BookingException.class, () -> bookingService.create(bookingDtoRequest, booker.getId()));
    }


    @Test
    void approve() {
        when(bookingRepository.findById(anyInt())).thenReturn(Optional.of(booking));

        BookingDto bookingDto = bookingService.approve(1, true, owner.getId());

        assertEquals(BookingStatus.APPROVED, bookingDto.getStatus());
        assertAll(
                () -> assertNotNull(bookingDto),
                () -> assertEquals(booking.getId(), bookingDto.getId()),
                () -> assertEquals(BookingStatus.APPROVED, bookingDto.getStatus())
        );
    }

    @Test
    void get() {
        when(bookingRepository.findById(anyInt())).thenReturn(Optional.of(booking));
        BookingDto bookingDto = bookingService.get(1, booker.getId());
        assertAll(
                () -> assertNotNull(bookingDto),
                () -> assertEquals(booking.getId(), bookingDto.getId())
        );
    }

    @Test
    void getAll() {
        when(bookingRepository.findAllByBookerId(anyInt())).thenReturn(List.of(booking));
        List<BookingDto> bookingDtoList = bookingService.getAll(booker.getId(), BookingState.ALL);
        assertAll(
                () -> assertNotNull(bookingDtoList),
                () -> assertEquals(1, bookingDtoList.size())
        );
    }

    @Test
    void getAllByOwner() {
        when(bookingRepository.findAllByBookerId(anyInt())).thenReturn(List.of(booking));

        List<BookingDto> bookingDtoList = bookingService.getAll(booker.getId(), BookingState.ALL);

        assertAll(
                () -> assertEquals(1, bookingDtoList.size()),
                () -> assertNotNull(bookingDtoList),
                () -> assertEquals(booking.getId(), bookingDtoList.getFirst().getId())
        );
    }

    @BeforeEach
    void setUp() {
        owner = User.builder()
                .id(1)
                .email("user@test.ru")
                .name("user")
                .build();
        item = Item.builder()
                .id(1)
                .name("Item name")
                .description("Item description")
                .available(true)
                .owner(owner)
                .build();
        booker = User.builder()
                .id(2)
                .email("booker@test.ru")
                .name("Booker name")
                .build();
        booking = Booking.builder()
                .id(1)
                .booker(booker)
                .item(item)
                .start(LocalDateTime.parse("2020-01-01T00:00:00"))
                .end(LocalDateTime.parse("2020-01-01T01:00:00"))
                .status(BookingStatus.WAITING)
                .build();
        bookingDto = BookingDto.builder()
                .id(1)
                .start(LocalDateTime.parse("2020-01-01T00:00:00"))
                .end(LocalDateTime.parse("2020-01-01T01:00:00"))
                .status(BookingStatus.WAITING)
                .build();
        bookingDtoRequest = BookingDtoRequest.builder()
                .itemId(1)
                .start(LocalDateTime.parse("2020-01-01T00:00:00"))
                .end(LocalDateTime.parse("2020-01-01T01:00:00"))
                .build();
    }
}

