package ru.practicum.shareit.request.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestMapper;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
class ItemRequestServiceImplTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    private User user;
    private ItemRequest itemRequest;
    private Item item;


    @BeforeEach
    void setUp() {
        user = User.builder().id(1).name("Name").email("123@email.ru").build();
        user = userRepository.save(user);
        item = Item.builder().id(1).name("Name").description("Description").available(true).build();
        item = itemRepository.save(item);
        itemRequest = ItemRequest.builder()
                .id(1).description("Text").requestor(user).created(LocalDateTime.now()).build();
        itemRequest = itemRequestRepository.save(itemRequest);
    }

    @Test
    void getAll() {
        List<ItemRequest> itemRequests = itemRequestRepository.findAll();
        List<Item> items = itemRepository.findAll();

        Assertions.assertEquals(itemRequests.size(), items.size());
        Assertions.assertEquals(itemRequests.getFirst().getRequestor(), user);
    }

    @Test
    void getByUserIdAndReqId() {
        Optional<ItemRequest> itemRequests = itemRequestRepository.findById(itemRequest.getId());

        Assertions.assertEquals(itemRequests.get().getRequestor(), user);

    }

    @Test
    void getByUserId() {
        List<ItemRequest> list = itemRequestRepository.findByRequestorIdOrderByCreatedDesc(user.getId());

        Assertions.assertEquals(list.size(), 1);
        Assertions.assertEquals(list.getFirst().getRequestor(), user);
        Assertions.assertEquals(list.getFirst().getCreated(), itemRequest.getCreated());
    }

    @Test
    void add() {
        User requestor = userRepository.findById(user.getId()).get();
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(ItemRequestMapper.toItemRequestDto(ItemRequest.builder().build()));
        itemRequest.setRequestor(requestor);

        Assertions.assertEquals(itemRequestRepository
                .findByRequestorIdOrderByCreatedDesc(requestor.getId()).getFirst().getRequestor(), requestor);
    }
}