package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItems;
import ru.practicum.shareit.request.mappers.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequest create(Long userId, ItemRequestDto itemRequest) {
        User user = userService.findById(userId);
        return itemRequestRepository.save(new ItemRequest(
                null,
                itemRequest.getDescription(),
                user,
                Timestamp.valueOf(LocalDate.now().atStartOfDay())
        ));
    }

    @Override
    public Collection<ItemRequestWithItems> getRequestsWithResponse(Long userId) {
        User user = userService.findById(userId);
        Collection<ItemRequest> itemRequests = itemRequestRepository.findAllByRequestor(user);
        Collection<Item> items = itemRepository.findAllByRequestIn(itemRequests);
        return itemRequestWithItemsMap(itemRequests, items);
    }

    @Override
    public Collection<ItemRequestWithItems> getAll() {
        Collection<ItemRequest> itemRequests = itemRequestRepository.findAll();
        Collection<Item> items = itemRepository.findAllByRequestIn(itemRequests);
        return itemRequestWithItemsMap(itemRequests, items);
    }

    @Override
    public ItemRequestWithItems getById(Long id) {
        ItemRequest itemRequest = itemRequestRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Запрос не найден!"));
        Collection<Item> items = itemRepository.findAllByRequestIn(Collections.singletonList(itemRequest));
        return ItemRequestMapper.itemRequestToDto(itemRequest, items);
    }

    private Collection<ItemRequestWithItems> itemRequestWithItemsMap(Collection<ItemRequest> itemRequests, Collection<Item> items) {
        Collection<ItemRequestWithItems> result = new LinkedList<>();
        for (ItemRequest itemRequest : itemRequests) {
            result.add(ItemRequestMapper.itemRequestToDto(itemRequest, items));
        }

        return result;
    }
}
