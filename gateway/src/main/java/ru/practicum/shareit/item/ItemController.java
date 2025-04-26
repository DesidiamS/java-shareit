package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequest;
import ru.practicum.shareit.item.dto.ItemDto;

import static ru.practicum.shareit.Constants.USER_HEADER;

@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> getAllItems(@RequestHeader(USER_HEADER) long userId) {
        return itemClient.getAllItems(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemById(@PathVariable("id") long itemId,
                                              @RequestHeader(USER_HEADER) long userId) {
        return itemClient.getItemById(itemId, userId);
    }

    @PostMapping
    public ResponseEntity<Object> createItem(@Valid @RequestBody ItemDto item,
                                             @RequestHeader(USER_HEADER) long userId) {
        return itemClient.createItem(item, userId);
    }

    @PatchMapping("{id}")
    public ResponseEntity<Object> updateItem(@PathVariable long id,
                                             @RequestHeader(USER_HEADER) long userId,
                                             @Valid @RequestBody ItemDto item) {
        return itemClient.updateItem(id, item, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestParam String query) {
        return itemClient.searchItem(query);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@Valid @RequestBody CommentRequest request,
                                                @PathVariable long itemId,
                                                @RequestHeader(USER_HEADER) long userId) {
        return itemClient.createComment(request, itemId, userId);
    }
}
