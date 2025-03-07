package ru.practicum.shareit.booking.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class BookingRequest {

    @NotNull
    Long itemId;
    @NotNull
    Timestamp start;
    @NotNull
    Timestamp end;
}
