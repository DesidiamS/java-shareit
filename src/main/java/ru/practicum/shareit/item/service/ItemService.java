package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithComment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.Collection;

public interface ItemService {

    Item createItem(User user, ItemDto itemDto);

    Item updateItem(User user, Long itemId, ItemDto itemDto);

    Collection<ItemWithComment> getAllItemsByUserId(User user);

    ItemWithComment getItemById(Long id);

    Collection<Item> searchItem(String searchString);

    CommentDto makeComment(CommentRequest request, Long itemId, Long userId);
}
