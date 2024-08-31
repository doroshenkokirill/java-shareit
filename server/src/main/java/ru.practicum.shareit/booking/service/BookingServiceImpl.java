package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.*;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.model.Item;
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
                .orElseThrow(() -> new NotFoundException("BookerId " + bookerId + " не найден"));

        Item item = itemRepository.findById(bookingDtoRequest.getItemId())
                .orElseThrow(() -> new NotFoundException("ItemId " + bookingDtoRequest.getItemId() + " не найден"));

        if (!item.getAvailable()) {
            throw new BookingException(String.format("Item " + item.getName() + " не доступен"));
        }

        return BookingMapper.toBookingDto(bookingRepository.save(
                BookingMapper.toBookingFromBookingRequest(bookingDtoRequest, item, booker, BookingStatus.WAITING)
        ));
    }

    @Override
    public BookingDto approve(int bookingId, boolean approved, int userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found with id: " + bookingId));
        if (booking.getItem().getOwner().getId() != userId) {
            throw new IllegalArgumentException("Only item owner can approve booking.");
        }
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto get(int bookingId, int userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found with id: " + bookingId));
        int bookerId = booking.getBooker().getId();
        int itemOwnerId = booking.getItem().getOwner().getId();
        if (userId != bookerId && userId != itemOwnerId) {
            throw new IllegalArgumentException("Only booker or item owner can view booking details.");
        }
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getAll(int userId, BookingState bookingState) {
        userService.validateById(userId);
        LocalDateTime currentTime = LocalDateTime.now();

        List<Booking> bookingList = switch (bookingState) {
            case BookingState.ALL -> bookingRepository.findAllByBookerId(userId);
            case BookingState.CURRENT -> bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStart(userId, currentTime, currentTime);
            case BookingState.PAST -> bookingRepository.findAllByBookerIdAndEndBeforeOrderByStart(userId, currentTime);
            case BookingState.FUTURE -> bookingRepository.findAllByBookerIdAndStartAfterOrderByStart(userId, currentTime);
            case BookingState.WAITING -> bookingRepository.findAllByBookerIdAndStatus(userId, BookingStatus.WAITING);
            default -> throw new IllegalArgumentException("State parameter is wrong.");
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
            case BookingState.ALL -> bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId);
            case BookingState.CURRENT -> bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, now, now);
            case BookingState.PAST -> bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(userId, now);
            case BookingState.FUTURE -> bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(userId, now);
            case BookingState.WAITING -> bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
            default -> throw new IllegalArgumentException("State parameter is wrong.");
        };

        return bookingList.stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }
}
