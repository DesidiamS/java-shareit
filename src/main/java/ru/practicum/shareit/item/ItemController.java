package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithComment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
import java.util.Objects;

import static ru.practicum.shareit.Constants.USER_HEADER;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;
    private final UserService userService;

    @GetMapping
    Collection<ItemWithComment> getAllItems(@RequestHeader HttpHeaders headers) {
        Long userId = Long.valueOf(Objects.requireNonNull(headers.get(USER_HEADER)).get(0));
        User user = userService.findById(userId);
        return itemService.getAllItemsByUserId(user);
    }

    @GetMapping("/{id}")
    ItemWithComment getItemById(@PathVariable Long id) {
        return itemService.getItemById(id);
    }

    @PostMapping
    public Item createItem(@RequestBody @Valid ItemDto itemDto,
                           @RequestHeader HttpHeaders headers) {
        log.info("creating item with name: " + itemDto.getName());
        Long userId = Long.valueOf(Objects.requireNonNull(headers.get(USER_HEADER)).get(0));
        User user = userService.findById(userId);
        return itemService.createItem(user, itemDto);
    }

    @PatchMapping("/{id}")
    public Item updateItem(@PathVariable Long id,
                           @RequestBody ItemDto itemDto,
                           @RequestHeader HttpHeaders headers) {
        log.info("updating item with id: " + id);
        Long userId = Long.valueOf(Objects.requireNonNull(headers.get(USER_HEADER)).get(0));
        User user = userService.findById(userId);
        return itemService.updateItem(user, id, itemDto);
    }

    @GetMapping("/search")
    public Collection<Item> itemSearch(@RequestParam String text) {
        log.info("search item by: " + text);
        return itemService.searchItem(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestBody CommentRequest request,
                                    @PathVariable Long itemId,
                                    @RequestHeader HttpHeaders headers) {
        Long userId = Long.valueOf(Objects.requireNonNull(headers.get(USER_HEADER)).get(0));
        return itemService.makeComment(request, itemId, userId);
    }

}
