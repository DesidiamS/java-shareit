package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static ru.practicum.shareit.Constants.USER_HEADER;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid ItemRequest itemRequest,
                                         @RequestHeader(USER_HEADER) long userId) {
        return itemRequestClient.createItem(itemRequest, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getByUser(@RequestHeader(USER_HEADER) long userId) {
        return itemRequestClient.getByUserId(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll() {
        return itemRequestClient.getAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") long id) {
        return itemRequestClient.getById(id);
    }
}
