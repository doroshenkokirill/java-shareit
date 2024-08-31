package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentDtoExp;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.comment.service.CommentMapper;
import ru.practicum.shareit.exceptions.AddCommentException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.WrongOwnerException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemDto add(ItemDto itemDto, int ownerId) {
        userService.validateById(ownerId);
        User owner = UserMapper.toUser(userService.getById(ownerId));
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(owner);
        Integer requestId = itemDto.getRequestId();
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("ItemRequestId не найден" + requestId)));
        item.setRequest(itemRequest);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }


    @Override
    public ItemDto update(int itemId, int ownerId, ItemDto itemDto) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item не найден: " + itemId));
        if (item.getOwner().getId() != ownerId) {
            throw new WrongOwnerException("Только владелец может вносить изменения");
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        Item updatedItem = itemRepository.save(item);
        return ItemMapper.toItemDto(updatedItem);
    }

    @Override
    public List<ItemDto> getAll(int ownerId) {
        return itemRepository.findAll().stream()
                .filter(item -> item.getOwner().getId() == ownerId)
                .map(ItemMapper::toItemDto)
                .toList();
    }


    @Override
    public ItemDto getById(int itemId, int userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item не найден: " + itemId));

        List<CommentDtoExp> comments = commentRepository.findAllByItemId(item.getId())
                .stream()
                .map(CommentMapper::toCommentDtoExp)
                .toList();

        ItemDto itemDto = ItemMapper.toItemDto(itemRepository.findById(itemId).get());
        itemDto.setComments(comments);

        if (item.getOwner().getId() == userId) {
            Optional<Booking> lastBooking = bookingRepository.findFirstByItemIdAndStartBeforeAndStatusNotOrderByStartDesc(itemId, LocalDateTime.now(), BookingStatus.REJECTED);
            lastBooking.ifPresent(booking -> itemDto.setLastBooking(BookingMapper.toBookingDto(booking)));
            Optional<Booking> nextBooking = bookingRepository.findFirstByItemIdAndStartAfterAndStatusNotOrderByStart(itemId, LocalDateTime.now(), BookingStatus.REJECTED);
            nextBooking.ifPresent(booking -> itemDto.setNextBooking(BookingMapper.toBookingDto(booking)));
        }
        return itemDto;
    }

    @Override
    public void validateById(int id) {
        if (!itemRepository.existsById(id)) {
            throw new NotFoundException(String.format("Item c id - %d не найден. Ошибка", id));
        }
    }

    @Override
    public List<ItemDto> search(String text) {
        if (Objects.equals(text, "")) return Collections.emptyList();

        return itemRepository.findAll().stream()
                .filter(Item::getAvailable)
                .filter(item -> item.getName() != null && item.getName().toLowerCase().contains(text.toLowerCase()) ||
                        item.getDescription() != null && item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public CommentDtoExp addComment(int itemId, int userId, CommentDto comment) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь c id - %d не найден. Ошибка", userId)));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Item c id - %d не найден. Ошибка", itemId)));
        List<Booking> bookings = bookingRepository.findAllByBookerIdAndItemId(userId, itemId);

        boolean isAbleToAddComment = bookings.stream()
                .anyMatch(booking -> booking.getEnd().isBefore(LocalDateTime.now())
                        && booking.getStatus().equals(BookingStatus.APPROVED));

        if (!isAbleToAddComment) {
            throw new AddCommentException("Нельзя добавить комментарий");
        }
        comment.setCreated(LocalDateTime.now());
        comment.setItem(item);
        comment.setAuthor(author);

        Comment savedComment = commentRepository.save(CommentMapper.toComment(comment));
        return CommentMapper.toCommentDtoExp(savedComment);
    }
}
