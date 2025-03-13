package ru.practicum.shareit.item.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithComment;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {

    public static Item dtoToItem(User user, ItemDto itemDto) {
        return new Item(itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                user,
                itemDto.getRequest());
    }

    public static ItemWithComment toItemWithComment(Item item, List<Comment> comments, Timestamp lastBooking,
                                                    Timestamp nextBooking) {
        return new ItemWithComment(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), item.getOwner(),
                item.getRequest(), lastBooking, nextBooking, comments);
    }
}
