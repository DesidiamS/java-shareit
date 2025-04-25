package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@EnableJpaRepositories
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(properties = {"spring.datasource.url=jdbc:h2:mem:shareit",
        "spring.datasource.username=sa", "spring.datasource.password=sa"})
public class BookingServiceDBTest {

    private final BookingServiceImpl bookingService;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Test
    public void whenCreateBookingThenReturnBooking() {
        User user = userRepository.save(new User(null, "John", "john@email.ru"));

        Item item = itemRepository.save(new Item("test", "test_description", true,
                user, null));

        BookingRequest bookingRequest = new BookingRequest(item.getId(), Timestamp.valueOf(LocalDateTime.now()),
                Timestamp.valueOf(LocalDateTime.now()));

        Booking booking = bookingService.createBooking(user.getId(), bookingRequest);

        assertThat(booking.getId(), notNullValue());
        assertThat(booking.getItem().getId(), equalTo(item.getId()));
        assertThat(booking.getBooker(), equalTo(user));
    }
}
