package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mappers.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final Map<Long, Item> itemList = new LinkedHashMap<>();
    private Long sequence = 0L;

    @Override
    public Item createItem(User user, ItemDto itemDto) {
        sequence += 1;

        Item item = ItemMapper.dtoToItem(sequence, user, itemDto);

        itemList.put(sequence, item);

        return item;
    }

    @Override
    public Item updateItem(User user, Long itemId, ItemDto itemDto) {
        getItemById(itemId);

        Item item = ItemMapper.dtoToItem(itemId, user, itemDto);

        itemList.replace(itemId, item);

        return getItemById(itemId);
    }

    @Override
    public List<Item> getAllItemsByUserId(User user) {
        List<Item> items = new LinkedList<>();
        for (Item item : itemList.values()) {
            if (item.getOwner().getId().equals(user.getId())) {
                items.add(item);
            }
        }
        return items;
    }

    @Override
    public Item getItemById(Long id) {
        Item item = itemList.get(id);

        if (item == null) {
            throw new NotFoundException("Предмет не найден!");
        }

        return item;
    }

    @Override
    public List<Item> searchItem(String searchString) {
        List<Item> items = new LinkedList<>();
        if (!searchString.isEmpty()) {
            for (Item item : itemList.values()) {
                if ((item.getName() != null && item.getName().toLowerCase().contains(searchString.toLowerCase())
                        || item.getDescription() != null && item.getDescription().toLowerCase()
                        .contains(searchString.toLowerCase())) && item.getAvailable()) {
                    items.add(item);
                }
            }
        }
        return items;
    }
}
