package ru.practicum.shareit.request.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * TODO Sprint add-item-requests.
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class ItemRequestDto {
    String description;
}
