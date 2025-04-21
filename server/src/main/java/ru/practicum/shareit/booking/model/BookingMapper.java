package ru.practicum.shareit.booking.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(booking.getStart(),
                booking.getEnd(),
                booking.getStatus(),
                booking.getBooker().getId(),
                booking.getItem().getId(),
                booking.getItem().getName());
    }
}
