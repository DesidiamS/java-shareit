package ru.practicum.shareit.booking.dto;


import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
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
    @FutureOrPresent
    Timestamp start;
    @NotNull
    @Future
    Timestamp end;
}
