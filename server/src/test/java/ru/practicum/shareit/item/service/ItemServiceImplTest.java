package ru.practicum.shareit.item.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentDtoExp;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
class ItemServiceImplTest {
    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CommentRepository commentRepository;

    private User user;
    private Item item;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1).name("User Name").email("123@test.ru").build();
        item = Item.builder().id(1).name("Item Name").description("Item Description").available(true).owner(user).build();
        user = userRepository.save(user);
        userRepository.save(user);
        itemRepository.save(item);
    }

    @Test
    void update() {
        ItemDto itemDtoNew = ItemDto.builder().id(1).name("New User Name").description("123@test.ru").build();
        ItemDto itemDto = itemService.update(item.getId(), user.getId(), itemDtoNew);

        assertEquals(itemDto.getName(), itemDtoNew.getName());

        Item updatedItem = itemRepository.findById(item.getId()).orElseThrow();

        assertEquals(updatedItem.getName(), itemDtoNew.getName());

    }

    @Test
    void getAll() {
        List<ItemDto> itemDtoList = itemService.getAll(user.getId());

        assertEquals(itemDtoList.getFirst().getName(), item.getName());
    }

    @Test
    void getById() {
        userRepository.save(user);
        ItemDto itemDto = itemService.getById(item.getId(), user.getId());

        assertThat(itemDto).isNotNull();
        assertThat(itemDto.getId()).isEqualTo(item.getId());
        assertThat(itemDto.getName()).isEqualTo(item.getName());
    }

    @Test
    void search() {
        List<ItemDto> itemDtoList = itemService.search("item");

        assertEquals(itemDtoList.size(), 1);
    }

    @Test
    void addComment() {
        userRepository.save(user);
        itemRepository.save(item);
        Booking booking = Booking.builder().id(1).item(item).booker(user)
                .start(LocalDateTime.parse("2020-01-01T00:00:00"))
                .end(LocalDateTime.parse("2020-01-01T01:00:00"))
                .status(BookingStatus.APPROVED)
                .build();

        bookingRepository.save(booking);
        CommentDto commentDto = CommentDto.builder().text("Comment Dto Text").build();
        CommentDtoExp commentDtoExp = itemService.addComment(item.getId(), user.getId(), commentDto);

        assertEquals(commentDtoExp.getText(), commentDto.getText());

        Comment comment1 = commentRepository.findById(commentDtoExp.getId()).orElseThrow();
        assertEquals(comment1.getText(), commentDto.getText());
    }
}