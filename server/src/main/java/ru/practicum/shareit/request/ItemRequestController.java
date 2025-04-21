package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItems;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.Collection;
import java.util.Objects;

import static ru.practicum.shareit.Constants.USER_HEADER;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequest createItemRequest(@RequestHeader HttpHeaders headers,
                                         @RequestBody ItemRequestDto itemRequest) {
        Long userId = Long.valueOf(Objects.requireNonNull(headers.get(USER_HEADER)).getFirst());
        return itemRequestService.create(userId, itemRequest);
    }

    @GetMapping
    public Collection<ItemRequestWithItems> getItemRequestsByUser(@RequestHeader HttpHeaders headers) {
        Long userId = Long.valueOf(Objects.requireNonNull(headers.get(USER_HEADER)).getFirst());
        return itemRequestService.getRequestsWithResponse(userId);
    }

    @GetMapping("/all")
    public Collection<ItemRequestWithItems> getAllItemRequests() {
        return itemRequestService.getAll();
    }

    @GetMapping("/{id}")
    public ItemRequestWithItems getItemRequestById(@PathVariable Long id) {
        return itemRequestService.getById(id);
    }
}
