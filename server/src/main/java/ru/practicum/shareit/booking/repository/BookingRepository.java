package ru.practicum.shareit.booking.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.sql.Timestamp;
import java.util.List;

@Repository
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

    @Query("SELECT MAX(b.end) FROM Booking b WHERE b.item.id = ?1 AND b.end < CURRENT_TIMESTAMP")
    Timestamp findLastBookingByItem(Long itemId);

    @Query("SELECT min(b.start) FROM Booking b WHERE b.item.id = ?1 AND b.start < CURRENT_TIMESTAMP")
    Timestamp findNextBookingByItem(Long itemId);

    @Query("SELECT count(b) > 0 FROM Booking b WHERE b.item.id = ?1 AND b.booker.id = ?2 AND b.end < ?3 AND b.status = 'APPROVED'")
    boolean isUserBookedItem(Long itemId, Long userId, Timestamp now);
}
