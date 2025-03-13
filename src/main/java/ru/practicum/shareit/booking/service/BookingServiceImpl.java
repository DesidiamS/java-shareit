package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public List<Booking> getBookingByBooker(Long bookerId, BookingState state) {
        Timestamp now = Timestamp.valueOf(LocalDate.now().atStartOfDay());
        if (state == null) {
            state = BookingState.ALL;
        }
        return switch (state) {
            case CURRENT ->
                    bookingRepository.findBookingsByBookerIdAndStartAfterAndEndBeforeOrderByIdDesc(bookerId, now, now);
            case PAST -> bookingRepository.findBookingsByBookerIdAndEndAfterOrderByIdDesc(bookerId, now);
            case FUTURE -> bookingRepository.findBookingsByBookerIdAndStartAfterOrderByIdDesc(bookerId, now);
            case WAITING, REJECTED -> bookingRepository.findBookingsByBookerIdAndStatusOrderByIdDesc(bookerId,
                    BookingStatus.valueOf(state.toString()));
            default -> bookingRepository.findBookingsByBookerIdOrderByIdDesc(bookerId);
        };
    }

    @Override
    public List<Booking> getBookingByOwner(Long ownerId, BookingState state) {
        Timestamp now = Timestamp.valueOf(LocalDate.now().atStartOfDay());
        if (state == null) {
            state = BookingState.ALL;
        }

        userRepository.findById(ownerId).orElseThrow(() -> new NotFoundException("Пользователь не найден!"));
        return switch (state) {
            case CURRENT ->
                    bookingRepository.findBookingsByItemOwnerIdAndStartAfterAndEndBeforeOrderByIdDesc(ownerId, now, now);
            case PAST -> bookingRepository.findBookingsByItemOwnerIdAndEndAfterOrderByIdDesc(ownerId, now);
            case FUTURE -> bookingRepository.findBookingsByItemOwnerIdAndStartAfterOrderByIdDesc(ownerId, now);
            case WAITING, REJECTED -> bookingRepository.findBookingsByItemOwnerIdAndStatusOrderByIdDesc(ownerId,
                    BookingStatus.valueOf(state.toString()));
            default -> bookingRepository.findBookingsByItemOwnerIdOrderByIdDesc(ownerId);
        };
    }

    @Override
    public Booking getBookingById(Long userId, Long id) {
        return bookingRepository.findBookingByBookerIdAndId(userId, id).orElseThrow(()
                -> new NotFoundException("Бронирование найдено!"));
    }

    @Override
    public Booking createBooking(Long userId, BookingRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден!"));
        Item item = itemRepository.findById(request.getItemId()).orElseThrow(() -> new NotFoundException("Вещь не найдена!"));
        if (!item.getAvailable()) {
            throw new ValidateException("Вещь недоступна для бронирования!");
        }

        Timestamp now = Timestamp.valueOf(LocalDate.now().atStartOfDay());

        if (request.getEnd().before(now)) {
            throw new ValidateException("Дата окончания не может быть меньше текущей!");
        }

        if (request.getStart().before(now)) {
            throw new ValidateException("Дата начала не может быть меньше текущей!");
        }

        if (request.getStart() == request.getEnd()) {
            throw new ValidateException("Дата начала не должна совпадать с датой окончания!");
        }

        return bookingRepository.save(new Booking(null, request.getStart(), request.getEnd(), item, user, BookingStatus.WAITING));
    }

    @Override
    public Booking updateBookingStatus(Long userId, Long bookingId, BookingStatus status) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(()
                -> new NotFoundException("Бронирование не найдено!"));
        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new ValidateException("Неверный пользователь!");
        }

        booking.setStatus(status);

        bookingRepository.save(booking);

        return booking;
    }
}
