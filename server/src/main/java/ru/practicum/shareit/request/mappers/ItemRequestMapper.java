package ru.practicum.shareit.request.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestWithItems;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;

@Mapper
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemRequestMapper {

    public static ItemRequestWithItems itemRequestToDto(ItemRequest itemRequest, Collection<Item> items) {
        return new ItemRequestWithItems(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getRequestor(),
                itemRequest.getCreated(),
                items);
    }
}
