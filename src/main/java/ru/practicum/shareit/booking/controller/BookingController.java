package ru.practicum.shareit.booking.controller;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;
import java.util.Objects;

import static ru.practicum.shareit.Constants.USER_HEADER;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public List<Booking> getBookingByBooker(@RequestHeader HttpHeaders headers,
                                            @RequestParam @Nullable BookingState state) {
        Long userId = Long.valueOf(Objects.requireNonNull(headers.get(USER_HEADER)).get(0));
        return bookingService.getBookingByBooker(userId, state);
    }

    @GetMapping("/owner")
    public List<Booking> getBookingByOwner(@RequestHeader HttpHeaders headers,
                                           @RequestParam @Nullable BookingState state) {
        Long userId = Long.valueOf(Objects.requireNonNull(headers.get(USER_HEADER)).get(0));
        return bookingService.getBookingByOwner(userId, state);
    }

    @GetMapping("/{bookingId}")
    public Booking getBookingById(@RequestHeader HttpHeaders headers,
                                  @PathVariable Long bookingId) {
        Long userId = Long.valueOf(Objects.requireNonNull(headers.get(USER_HEADER)).get(0));
        return bookingService.getBookingById(userId, bookingId);
    }

    @PostMapping
    public Booking createBooking(@RequestHeader HttpHeaders headers,
                                 @RequestBody BookingRequest request) {
        Long userId = Long.valueOf(Objects.requireNonNull(headers.get(USER_HEADER)).get(0));
        return bookingService.createBooking(userId, request);
    }

    @PatchMapping("/{bookingId}")
    public Booking changeBookingStatus(@RequestHeader HttpHeaders headers,
                                       @PathVariable Long bookingId,
                                       @RequestParam Boolean approved) {
        Long userId = Long.valueOf(Objects.requireNonNull(headers.get(USER_HEADER)).get(0));
        BookingStatus status;
        if (approved) {
            status = BookingStatus.APPROVED;
        } else {
            status = BookingStatus.REJECTED;
        }
        return bookingService.updateBookingStatus(userId, bookingId, status);
    }

}
