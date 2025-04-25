package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;

@Transactional
@EnableJpaRepositories
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(properties = {"spring.datasource.url=jdbc:postgresql://localhost:5432/shareit",
        "spring.datasource.username=postgres", "spring.datasource.password=postgres"})
public class RequestServiceDBTest {

    private final ItemRequestService itemRequestService;
    private final UserRepository userRepository;

    @Test
    public void whenCreateThenReturnItemRequest() {
        Long userId = userRepository.save(new User(null, "John", "john@email.ru")).getId();

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("test description");

        ItemRequest itemRequest = itemRequestService.create(userId, itemRequestDto);

        assertThat(itemRequest.getId(), notNullValue());
        assertThat(itemRequest.getRequestor().getId(), equalTo(userId));
        assertThat(itemRequest.getDescription(), equalTo("test description"));
    }
}
