package ru.practicum.shareit.booking.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findBookingsByBookerIdAndStartAfterOrderByIdDesc(Long bookerId, Timestamp now);

    List<Booking> findBookingsByBookerIdAndEndAfterOrderByIdDesc(Long bookerId, Timestamp now);

    List<Booking> findBookingsByBookerIdAndStatusOrderByIdDesc(Long bookerId, BookingStatus status);

    List<Booking> findBookingsByBookerIdAndStartAfterAndEndBeforeOrderByIdDesc(Long bookerId, Timestamp nowStart,
                                                                               Timestamp nowEnd);

    List<Booking> findBookingsByBookerIdOrderByIdDesc(Long bookerId);

    List<Booking> findBookingsByItemOwnerIdAndStartAfterOrderByIdDesc(Long ownerId, Timestamp now);

    List<Booking> findBookingsByItemOwnerIdAndEndAfterOrderByIdDesc(Long ownerId, Timestamp now);

    List<Booking> findBookingsByItemOwnerIdAndStatusOrderByIdDesc(Long ownerId, BookingStatus status);

    List<Booking> findBookingsByItemOwnerIdAndStartAfterAndEndBeforeOrderByIdDesc(Long ownerId, Timestamp nowStart,
                                                                                  Timestamp nowEnd);

    List<Booking> findBookingsByItemOwnerIdOrderByIdDesc(Long ownerId);

    Optional<Booking> findBookingByBookerIdAndId(Long bookerId, Long id);

    Optional<Booking> findBookingByItem(Item item);

    @Query("SELECT MAX(b.end) FROM Booking b WHERE b.item.id = ?1 AND b.end < CURRENT_TIMESTAMP")
    Timestamp findLastBookingByItem(Long itemId);

    @Query("SELECT min(b.start) FROM Booking b WHERE b.item.id = ?1 AND b.start < CURRENT_TIMESTAMP")
    Timestamp findNextBookingByItem(Long itemId);
}
