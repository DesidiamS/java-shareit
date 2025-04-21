package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.sql.Timestamp;
import java.util.Collection;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestWithItems {

    Long id;
    @Size(max = 250)
    String description;
    User requestor;
    Timestamp created;
    Collection<Item> items;
}
