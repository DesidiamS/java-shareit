package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.Constants.USER_HEADER;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    private MockMvc mvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mvc = MockMvcBuilders.standaloneSetup(bookingController).build();
    }

    @Test
    void whenGetBookingByBookerThenReturnOk() throws Exception {
        Long userId = 1L;
        BookingState state = BookingState.ALL;
        List<Booking> mockBookings = List.of(new Booking());

        Mockito.when(bookingService.getBookingByBooker(userId, state)).thenReturn(mockBookings);

        mvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, userId.toString())
                        .param("state", state.toString()))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetBookingByOwnerThenReturnOk() throws Exception {
        Long userId = 1L;
        BookingState state = BookingState.ALL;
        List<Booking> mockBookings = List.of(new Booking());

        Mockito.when(bookingService.getBookingByOwner(userId, state)).thenReturn(mockBookings);

        mvc.perform(get("/bookings/owner")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, userId.toString())
                        .param("state", state.toString()))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetBookingByIdThenReturnOk() throws Exception {
        Long userId = 1L;
        Long bookingId = 1L;
        Booking mockBooking = new Booking();

        Mockito.when(bookingService.getBookingById(userId, bookingId)).thenReturn(mockBooking);

        mvc.perform(get("/bookings/{bookingId}", bookingId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, userId.toString()))
                .andExpect(status().isOk());
    }

    @Test
    void whenCreateBookingThenReturnBooking() throws Exception {
        long userId = 1L;
        BookingRequest request = new BookingRequest(1L, Timestamp.valueOf(LocalDateTime.now()), Timestamp.valueOf(LocalDateTime.now().plusDays(1)));
        String jsonRequestBody = objectMapper.writeValueAsString(request);

        Booking createdBooking = new Booking();
        createdBooking.setId(1L);
        Mockito.when(bookingService.createBooking(any(), any(BookingRequest.class))).thenReturn(createdBooking);

        mvc.perform(post("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, Long.toString(userId))
                        .content(jsonRequestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)));
    }

    @Test
    void whenChangeBookingStatusToApprovedThenReturnOk() throws Exception {
        Long userId = 1L;
        Long bookingId = 1L;
        Booking booking = new Booking();
        booking.setId(bookingId);

        Mockito.when(bookingService.updateBookingStatus(userId, bookingId, BookingStatus.APPROVED))
                .thenReturn(booking);

        mvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, userId.toString())
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void whenChangeBookingStatusToRejectedThenReturnOk() throws Exception {
        Long userId = 1L;
        Long bookingId = 1L;
        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setStatus(BookingStatus.REJECTED);

        Mockito.when(bookingService.updateBookingStatus(userId, bookingId, BookingStatus.REJECTED))
                .thenReturn(booking);

        mvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, userId.toString())
                        .param("approved", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.status", equalTo(BookingStatus.REJECTED.toString())));
    }
}
