package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    List<ItemRequestDto> getAll(int userId);

    ItemRequestDto getByUserIdAndReqId(int userId, int requestId);

    List<ItemRequestDto> getByUserId(int userId);

    ItemRequestDto add(ItemRequestDto requestDto, int userId);

    void validateUser(int userId);

    void validateRequest(int requestId);
}
