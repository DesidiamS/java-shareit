package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface ItemService {

    Item createItem(User user, ItemDto itemDto);

    Item updateItem(User user, Long itemId, ItemDto itemDto);

    List<Item> getAllItemsByUserId(User user);

    Item getItemById(Long id);

    List<Item> searchItem(String searchString);
}
