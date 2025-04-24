package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.Constants.USER_HEADER;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {

    @Mock
    private BookingClient bookingClient;

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
    public void whenGetBookingsThenReturnBookings() throws Exception {
        Mockito
                .when(bookingClient.getBookings(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON).body(null));
        mvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, 1L)
                        .param("state", "all")
                        .param("from", "1")
                        .param("size", "20")
                )
                .andExpect(status().isOk());
    }

    @Test
    public void whenDontHaveUserThenReturnBadRequest() throws Exception {
        mvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("state", "all")
                        .param("from", "1")
                        .param("size", "20")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenWrongStateThenReturnBadRequest() {
        assertThrows(Exception.class, () -> mvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, 1L)
                        .param("state", "wrong")
                        .param("from", "1")
                        .param("size", "20")
                ).andExpect(status().isOk())
        );
    }

    @Test
    public void whenBookItemThenReturnOk() throws Exception {
        Mockito.when(bookingClient.bookItem(anyLong(), any()))
                .thenReturn(ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Collections.emptyMap()));

        BookItemRequestDto requestBody = new BookItemRequestDto(1L,
                Timestamp.valueOf(LocalDateTime.now().plusDays(1)),
                Timestamp.valueOf(LocalDateTime.now().plusDays(2)));

        String jsonRequestBody = objectMapper.writeValueAsString(requestBody);


        mvc.perform(post("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, 1L)
                        .content(jsonRequestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenInvalidRequestBodyThenReturnBadRequest() throws Exception {
        BookItemRequestDto invalidRequestBody = new BookItemRequestDto(1L, Timestamp.valueOf(LocalDateTime.now()),
                Timestamp.valueOf(LocalDateTime.now().minusHours(1)));
        String jsonRequestBody = objectMapper.writeValueAsString(invalidRequestBody);

        mvc.perform(post("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, 1L)
                        .content(jsonRequestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenMissingUserHeaderThenReturnBadRequest() throws Exception {
        BookItemRequestDto requestBody = new BookItemRequestDto(1L, Timestamp.valueOf(LocalDateTime.now().plusDays(1)),
                Timestamp.valueOf(LocalDateTime.now().plusDays(2)));
        String jsonRequestBody = objectMapper.writeValueAsString(requestBody);

        mvc.perform(post("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenGetBookingsThenReturnOk() throws Exception {
        Mockito.when(bookingClient.getBookings(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Collections.emptyList()));

        mvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, 1L)
                        .param("state", "all")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenGetBookingByIdThenReturnOk() throws Exception {
        Mockito.when(bookingClient.getBooking(anyLong(), anyLong()))
                .thenReturn(ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Collections.emptyMap()));

        mvc.perform(get("/bookings/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenBookingNotFoundThenReturnNotFound() throws Exception {
        Mockito.when(bookingClient.getBooking(anyLong(), anyLong()))
                .thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        mvc.perform(get("/bookings/999")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenMissingUserHeaderGetBookingByIdThenReturnBadRequest() throws Exception {
        mvc.perform(get("/bookings/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenGetBookingByOwnerThenReturnOk() throws Exception {
        Mockito.when(bookingClient.getBookingByOwner(anyLong(), any()))
                .thenReturn(ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(null));

        mvc.perform(get("/bookings/owner")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenChangeBookingStatusThenReturnOk() throws Exception {
        Mockito.when(bookingClient.bookingChangeStatus(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Collections.emptyMap()));

        mvc.perform(patch("/bookings/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenInvalidApprovedValueThenReturnBadRequest() throws Exception {
        mvc.perform(patch("/bookings/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, 1L)
                        .param("approved", "invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenMissingUserHeaderForChangeStatusThenReturnBadRequest() throws Exception {
        mvc.perform(patch("/bookings/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("approved", "true"))
                .andExpect(status().isBadRequest());
    }

}
