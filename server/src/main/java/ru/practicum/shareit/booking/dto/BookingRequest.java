package ru.practicum.shareit.booking.dto;


import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
