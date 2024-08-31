package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
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

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public List<ItemRequestDto> getAll(int userId) {
        validateUser(userId);
        List<ItemRequest> list = itemRequestRepository.findByRequestorIdNotOrderByCreatedDesc(userId);
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
        return requests.stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .peek(requestDto -> {
                    List<ItemDto> itemDtoList = itemRepository.findByRequestId(requestDto.getId())
                            .stream()
                            .map(ItemMapper::toItemDto)
                            .toList();

                    requestDto.setItems(itemDtoList);
                })
                .toList();
    }

}
