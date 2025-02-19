package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.sql.Timestamp;

/**
 * TODO Sprint add-bookings.
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking {

    Long id;
    Timestamp start;
    Timestamp end;
    Item item;
    User booker;
    BookingStatus status;
}
