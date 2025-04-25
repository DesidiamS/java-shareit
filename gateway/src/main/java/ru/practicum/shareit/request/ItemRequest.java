package ru.practicum.shareit.request;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.User;

import java.sql.Timestamp;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequest {

    Long id;
    @Size(max = 250)
    String description;
    User requestor;
    Timestamp created;
}
