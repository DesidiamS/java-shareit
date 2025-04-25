package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.CommentRequest;
import ru.practicum.shareit.item.dto.ItemWithComment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@Transactional
@EnableJpaRepositories
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(properties = {"spring.datasource.url=jdbc:h2:mem:shareit",
        "spring.datasource.username=sa", "spring.datasource.password=sa"})
public class ItemServiceDBTest {

    private final ItemServiceImpl itemService;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingService bookingService;

    @Test
    public void whenAddItemsThenReturnItems() {
        User user = userRepository.save(new User(null, "John", "john@email.ru"));

        itemRepository.save(new Item("test", "test_description", true,
                user, null));

        itemRepository.save(new Item("test2", "test_description2", false,
                user, null));

        List<ItemWithComment> allItems = new LinkedList<>(itemService.getAllItemsByUserId(user).stream().toList());

        allItems.sort(Comparator.comparingLong(ItemWithComment::getId));

        assertThat(allItems.size(), equalTo(2));
        assertThat(allItems.getFirst().getName(), equalTo("test"));
        assertThat(allItems.getFirst().getDescription(), equalTo("test_description"));
        assertThat(allItems.getLast().getName(), equalTo("test2"));
        assertThat(allItems.getLast().getDescription(), equalTo("test_description2"));
        assertThat(allItems.getFirst().getAvailable(), equalTo(true));
        assertThat(allItems.getLast().getAvailable(), equalTo(false));

    }

    @Test
    public void whenMakeCommentThenReturnComment() {
        User owner = userRepository.save(new User(null, "John", "john@email.ru"));
        Item item = itemRepository.save(new Item("test", "test_description", true,
                owner, null));

        User user = userRepository.save(new User(null, "Ivan", "ivan@email.ru"));

        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setText("test_comment");

        BookingRequest bookingRequest = new BookingRequest(item.getId(), Timestamp.valueOf(LocalDateTime.now()),
                Timestamp.valueOf(LocalDateTime.now()));

        bookingService.createBooking(user.getId(), bookingRequest);

        itemService.makeComment(commentRequest, item.getId(), user.getId());

        ItemWithComment itemWithComment = itemService.getItemById(item.getId());

        assertThat(itemWithComment.getComments().size(), equalTo(1));
        assertThat(itemWithComment.getComments().getFirst().getText(), equalTo("test_comment"));
    }
}
