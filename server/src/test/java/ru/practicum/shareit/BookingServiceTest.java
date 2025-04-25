package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository mockBookingRepository;

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private ItemRepository mockItemRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    public void whenGetBookingWithCurrentStateThenReturnBooking() {
        Long bookerId = 1L;
        List<Booking> mockBookings = new ArrayList<>();
        mockBookings.add(new Booking());

        Mockito
                .when(mockBookingRepository.findBookingsByBookerIdAndStartAfterAndEndBeforeOrderByIdDesc(anyLong(), any(Timestamp.class), any(Timestamp.class)))
                .thenReturn(mockBookings);

        List<Booking> result = bookingService.getBookingByBooker(bookerId, BookingState.CURRENT);

        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));
    }

    @Test
    void whenGetBookingByBookerWithStatePastThenReturnBookings() {
        Long bookerId = 1L;
        BookingState state = BookingState.PAST;
        Timestamp now = Timestamp.valueOf(LocalDate.now().atStartOfDay());
        List<Booking> mockBookings = List.of(new Booking());

        Mockito.when(mockBookingRepository.findBookingsByBookerIdAndEndAfterOrderByIdDesc(bookerId, now))
                .thenReturn(mockBookings);

        List<Booking> result = bookingService.getBookingByBooker(bookerId, state);

        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));
    }

    @Test
    void whenGetBookingByBookerWithDefaultStateThenReturnAllBookings() {
        Long bookerId = 1L;
        BookingState state = BookingState.ALL;
        List<Booking> mockBookings = List.of(new Booking());

        Mockito.when(mockBookingRepository.findBookingsByBookerIdOrderByIdDesc(bookerId))
                .thenReturn(mockBookings);

        List<Booking> result = bookingService.getBookingByBooker(bookerId, state);

        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));
    }

    @Test
    void whenGetBookingByBookerWithStateFutureThenReturnBookings() {
        Long bookerId = 1L;
        BookingState state = BookingState.FUTURE;
        Timestamp now = Timestamp.valueOf(LocalDate.now().atStartOfDay());
        List<Booking> mockBookings = List.of(new Booking());

        Mockito.when(mockBookingRepository.findBookingsByBookerIdAndStartAfterOrderByIdDesc(bookerId, now))
                .thenReturn(mockBookings);

        List<Booking> result = bookingService.getBookingByBooker(bookerId, state);

        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));
        Mockito.verify(mockBookingRepository).findBookingsByBookerIdAndStartAfterOrderByIdDesc(bookerId, now);
    }

    @Test
    void whenGetBookingByBookerWithStateWaitingThenReturnBookings() {
        Long bookerId = 1L;
        BookingState state = BookingState.WAITING;
        List<Booking> mockBookings = List.of(new Booking());

        Mockito.when(mockBookingRepository.findBookingsByBookerIdAndStatusOrderByIdDesc(bookerId, BookingStatus.WAITING))
                .thenReturn(mockBookings);

        List<Booking> result = bookingService.getBookingByBooker(bookerId, state);

        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));
        Mockito.verify(mockBookingRepository).findBookingsByBookerIdAndStatusOrderByIdDesc(bookerId, BookingStatus.WAITING);
    }

    @Test
    void whenGetBookingByBookerWithStateRejectedThenReturnBookings() {
        Long bookerId = 1L;
        BookingState state = BookingState.REJECTED;
        List<Booking> mockBookings = List.of(new Booking());

        Mockito.when(mockBookingRepository.findBookingsByBookerIdAndStatusOrderByIdDesc(bookerId, BookingStatus.REJECTED))
                .thenReturn(mockBookings);

        List<Booking> result = bookingService.getBookingByBooker(bookerId, state);

        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));
        Mockito.verify(mockBookingRepository).findBookingsByBookerIdAndStatusOrderByIdDesc(bookerId, BookingStatus.REJECTED);
    }

    @Test
    void whenGetBookingByOwnerWithStateCurrentThenReturnBookings() {
        Long ownerId = 1L;
        BookingState state = BookingState.CURRENT;
        Timestamp now = Timestamp.valueOf(LocalDate.now().atStartOfDay());
        List<Booking> mockBookings = List.of(new Booking());

        Mockito.when(mockUserRepository.findById(ownerId)).thenReturn(Optional.of(new User()));
        Mockito.when(mockBookingRepository.findBookingsByItemOwnerIdAndStartAfterAndEndBeforeOrderByIdDesc(ownerId, now, now))
                .thenReturn(mockBookings);

        List<Booking> result = bookingService.getBookingByOwner(ownerId, state);

        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));
    }

    @Test
    void whenGetBookingByOwnerWithStatePastThenReturnBookings() {
        Long ownerId = 1L;
        BookingState state = BookingState.PAST;
        Timestamp now = Timestamp.valueOf(LocalDate.now().atStartOfDay());
        List<Booking> mockBookings = List.of(new Booking());

        Mockito.when(mockUserRepository.findById(ownerId)).thenReturn(Optional.of(new User()));
        Mockito.when(mockBookingRepository.findBookingsByItemOwnerIdAndEndAfterOrderByIdDesc(ownerId, now))
                .thenReturn(mockBookings);

        List<Booking> result = bookingService.getBookingByOwner(ownerId, state);

        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));
    }

    @Test
    void whenGetBookingByOwnerWithStateFutureThenReturnBookings() {
        Long ownerId = 1L;
        BookingState state = BookingState.FUTURE;
        Timestamp now = Timestamp.valueOf(LocalDate.now().atStartOfDay());
        List<Booking> mockBookings = List.of(new Booking());

        Mockito.when(mockUserRepository.findById(ownerId)).thenReturn(Optional.of(new User()));
        Mockito.when(mockBookingRepository.findBookingsByItemOwnerIdAndStartAfterOrderByIdDesc(ownerId, now))
                .thenReturn(mockBookings);

        List<Booking> result = bookingService.getBookingByOwner(ownerId, state);

        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));
    }

    @Test
    void whenGetBookingByOwnerWithStateWaitingThenReturnBookings() {
        Long ownerId = 1L;
        BookingState state = BookingState.WAITING;
        List<Booking> mockBookings = List.of(new Booking());

        Mockito.when(mockUserRepository.findById(ownerId)).thenReturn(Optional.of(new User()));
        Mockito.when(mockBookingRepository.findBookingsByItemOwnerIdAndStatusOrderByIdDesc(ownerId, BookingStatus.WAITING))
                .thenReturn(mockBookings);

        List<Booking> result = bookingService.getBookingByOwner(ownerId, state);

        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));
    }

    @Test
    void whenGetBookingByOwnerWithStateRejectedThenReturnBookings() {
        Long ownerId = 1L;
        BookingState state = BookingState.REJECTED;
        List<Booking> mockBookings = List.of(new Booking());

        Mockito.when(mockUserRepository.findById(ownerId)).thenReturn(Optional.of(new User()));
        Mockito.when(mockBookingRepository.findBookingsByItemOwnerIdAndStatusOrderByIdDesc(ownerId, BookingStatus.REJECTED))
                .thenReturn(mockBookings);

        List<Booking> result = bookingService.getBookingByOwner(ownerId, state);

        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));
    }

    @Test
    void whenGetBookingByOwnerWithNonExistentUserThenThrowException() {
        Long ownerId = 999L;
        BookingState state = BookingState.ALL;

        Mockito.when(mockUserRepository.findById(ownerId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.getBookingByOwner(ownerId, state));
    }

    @Test
    void whenGetBookingByOwnerWithStateAllThenReturnAllBookings() {
        Long ownerId = 1L;
        BookingState state = BookingState.ALL;
        List<Booking> mockBookings = List.of(new Booking());

        Mockito.when(mockUserRepository.findById(ownerId)).thenReturn(Optional.of(new User()));
        Mockito.when(mockBookingRepository.findBookingsByItemOwnerIdOrderByIdDesc(ownerId))
                .thenReturn(mockBookings);

        List<Booking> result = bookingService.getBookingByOwner(ownerId, state);

        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));
        Mockito.verify(mockBookingRepository).findBookingsByItemOwnerIdOrderByIdDesc(ownerId);
    }

    @Test
    public void whenGetBookingByIdThenReturnBooking() {
        Booking booking = new Booking();

        Mockito
                .when(mockBookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));

        Booking result = bookingService.getBookingById(1L, 1L);

        assertThat(result, notNullValue());
    }

    @Test
    public void whenNotFoundBookingByIdThenThrowException() {
        Mockito
                .when(mockBookingRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.getBookingById(1L, 1L));
    }

    @Test
    public void whenCreateBookingThenReturnBooking() {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());

        Item item = new Item("test", "test_description", true, new User(), null);

        Booking booking = new Booking();

        Mockito
                .when(mockBookingRepository.save(any(Booking.class)))
                .thenReturn(booking);

        Mockito
                .when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));

        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(new User()));

        BookingRequest request = new BookingRequest(1L, now, now);

        Booking result = bookingService.createBooking(1L, request);

        assertThat(result, notNullValue());
    }

    @Test
    public void whenCreateWithNoAvailableItemThrowValidateException() {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());

        Item item = new Item("test", "test_description", false, new User(), null);
        Mockito
                .when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));

        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(new User()));

        BookingRequest request = new BookingRequest(1L, now, now);

        assertThrows(ValidateException.class, () -> bookingService.createBooking(1L, request));
    }

    @Test
    public void whenUpdateBookingThenReturnBooking() {
        User user = new User();
        user.setId(1L);

        Item item = new Item("test", "test_description", true, user, null);
        Booking booking = new Booking();
        booking.setItem(item);
        Mockito.when(mockBookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));

        Mockito.when(mockBookingRepository.save(any(Booking.class)))
                .thenReturn(new Booking());

        Booking newBooking = bookingService.updateBookingStatus(1L, 1L, BookingStatus.WAITING);

        assertThat(newBooking, notNullValue());
    }

    @Test
    public void whenUpdateBookingWithWrongBookingIdThenThrowException() {
        Mockito.when(mockBookingRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.updateBookingStatus(1L, 1L, BookingStatus.WAITING));
    }

    @Test
    public void whenUpdateBookingWithWrongUserThenThrowException() {
        User user = new User();
        user.setId(1L);
        User owner = new User();
        owner.setId(2L);

        Booking booking = new Booking();
        Item item = new Item("test", "test_description", true, owner, null);

        booking.setItem(item);

        Mockito.when(mockBookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));

        assertThrows(ValidateException.class, () -> bookingService.updateBookingStatus(user.getId(), 1L, BookingStatus.WAITING));
    }
}
