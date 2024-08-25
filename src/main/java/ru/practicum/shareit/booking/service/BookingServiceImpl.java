package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.BookingException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public BookingDto create(BookingDtoRequest bookingDtoRequest, int bookerId) {
        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException(String.format("Booker с id %d не найден. Ошибка", bookerId)));

        Item item = itemRepository.findById(bookingDtoRequest.getItemId())
                .orElseThrow(() -> new NotFoundException(String.format("Предмет с id %d не найдена. Ошибка", bookingDtoRequest.getItemId())));

        if (!item.getAvailable()) {
            throw new BookingException(String.format("Предмет %s не доступен. Ошибка", item.getName()));
        }

        return BookingMapper.toBookingDto(bookingRepository.save(
                BookingMapper.toBookingFromBookingRequest(bookingDtoRequest, item, booker, BookingStatus.WAITING)
        ));
    }

    @Override
    public BookingDto get(int bookingId, int userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("Бронирование с id %d не найдено. Ошибка", bookingId)));
        int bookerId = booking.getBooker().getId();
        int itemOwnerId = booking.getItem().getOwner().getId();
        if (userId != bookerId && userId != itemOwnerId) {
            throw new IllegalArgumentException("Только у owner`а или booker`а есть права. Ошибка");
        }
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getAll(int userId, BookingState bookingState) {
        userService.validateById(userId);
        LocalDateTime currentTime = LocalDateTime.now();

        List<Booking> bookingList = switch (bookingState) {
            case ALL -> bookingRepository.findByBookerId(userId);
            case CURRENT ->
                    bookingRepository.findByBookerIdAndStartBeforeAndEndAfterOrderByStart(userId, currentTime, currentTime);
            case PAST -> bookingRepository.findByBookerIdAndEndBeforeOrderByStart(userId, currentTime);
            case FUTURE -> bookingRepository.findByBookerIdAndStartAfterOrderByStart(userId, currentTime);
            case WAITING -> bookingRepository.findByBookerIdAndStatus(userId, BookingStatus.WAITING);
            case REJECTED -> throw new IllegalArgumentException("Отклонено. Ошибка");
        };

        return bookingList.stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }

    @Override
    public List<BookingDto> getAllByOwner(int userId, BookingState bookingState) {
        userService.validateById(userId);
        LocalDateTime now = LocalDateTime.now();

        List<Booking> bookingList = switch (bookingState) {
            case ALL -> bookingRepository.findByItemOwnerIdOrderByStartDesc(userId);
            case CURRENT ->
                    bookingRepository.findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, now, now);
            case PAST -> bookingRepository.findByItemOwnerIdAndEndBeforeOrderByStartDesc(userId, now);
            case FUTURE -> bookingRepository.findByItemOwnerIdAndStartAfterOrderByStartDesc(userId, now);
            case WAITING -> bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
            case REJECTED -> throw new IllegalArgumentException("Отклонено. Ошибка");
        };

        return bookingList.stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }

    @Override
    public BookingDto approve(int bookingId, boolean approved, int userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("Бронирование с id %d не найдено. Ошибка", bookingId)));
        if (booking.getItem().getOwner().getId() != userId) {
            throw new IllegalArgumentException("Только владелец вещи может подтвердить бронирование. Ошибка");
        }
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return BookingMapper.toBookingDto(booking);
    }
}
