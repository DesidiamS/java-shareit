package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItems;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;

public interface ItemRequestService {
    ItemRequest create(Long userId, ItemRequestDto itemRequest);

    Collection<ItemRequestWithItems> getRequestsWithResponse(Long userId);

    Collection<ItemRequestWithItems> getAll();

    ItemRequestWithItems getById(Long id);
}
