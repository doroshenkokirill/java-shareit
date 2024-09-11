package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestMapper;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final CommentRepository commentRepository;

    @Override
    public List<ItemRequestDto> getAll(int userId) {
        validateUser(userId);
        List<ItemRequest> list = itemRequestRepository.findByRequestorIdOrderByCreatedDesc(userId);
        return getItemDtosToRequestList(list);
    }

    @Override
    public ItemRequestDto getByUserIdAndReqId(int userId, int requestId) {
        validateUser(userId);
        validateRequest(requestId);
        ItemRequest req = itemRequestRepository.findById(requestId).get();
        return getItemDtosToRequestList(List.of(req)).getFirst();
    }

    @Override
    public List<ItemRequestDto> getByUserId(int userId) {
        validateUser(userId);
        List<ItemRequest> list = itemRequestRepository.findByRequestorIdOrderByCreatedDesc(userId);
        return getItemDtosToRequestList(list);
    }

    @Override
    public ItemRequestDto add(ItemRequestDto requestDto, int userId) {
        validateUser(userId);
        User requestor = userRepository.findById(userId).get();
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(requestDto);
        itemRequest.setRequestor(requestor);
        itemRequest.setCreated(LocalDateTime.now());
        return ItemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public void validateUser(int userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("Нет user`a с id - %d. Ошибка", userId));
        }
    }

    @Override
    public void validateRequest(int requestId) {
        if (!itemRequestRepository.existsById(requestId)) {
            throw new NotFoundException(String.format("Нет request`a с id - %d. Ошибка", requestId));
        }
    }

    private List<ItemRequestDto> getItemDtosToRequestList(List<ItemRequest> requests) {
        Set<Integer> requestIds = requests.stream()
                .map(ItemRequest::getId)
                .collect(Collectors.toSet());
        log.info("Список всех requestIds: {}", requestIds);

        List<Item> items = itemRepository.findItemsByRequestIdIn(requestIds);

        Map<Integer, List<ItemDto>> itemsByRequestsId = items.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.groupingBy(ItemDto::getRequestId));
        log.info("Сгруппированные по RequestsId: {}", itemsByRequestsId);

        return requests.stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .peek(requestDto -> {
                    int requestId = requestDto.getId();
                    log.info("RequestId: {}", requestId);
                    List<ItemDto> itemDtoList = itemsByRequestsId.getOrDefault(requestId, List.of());
                    log.info("Items найденные по RequestId {}: {}", requestId, itemDtoList);
                    requestDto.setItems(itemDtoList);
                })
                .collect(Collectors.toList());
    }
}
