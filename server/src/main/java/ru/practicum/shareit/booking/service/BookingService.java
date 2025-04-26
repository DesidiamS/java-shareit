package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.List;

public interface BookingService {

    List<Booking> getBookingByOwner(Long ownerId, BookingState state);

    List<Booking> getBookingByBooker(Long bookerId, BookingState state);

    Booking getBookingById(Long userId, Long id);

    Booking createBooking(Long userId, BookingRequest request);

    Booking updateBookingStatus(Long userId, Long bookingId, BookingStatus status);
}
