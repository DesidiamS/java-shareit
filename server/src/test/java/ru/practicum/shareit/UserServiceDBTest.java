package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@EnableJpaRepositories
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(properties = {"spring.datasource.url=jdbc:h2:mem:shareit",
        "spring.datasource.username=sa", "spring.datasource.password=sa"})
@SpringJUnitConfig({UserRepository.class, UserServiceImpl.class, ShareItServer.class})
public class UserServiceDBTest {

    private final UserRepository userRepository;
    private final UserServiceImpl userService;

    @Test
    public void whenAskUserThenReturnUser() {
        Long id = userService.createUser(new User(null, "test_name", "test_email"))
                .getId();
        User user = userRepository.findById(id).orElse(null);
        assertThat(user, notNullValue());
    }


    @Test
    public void whenCreateUserThenReturnUser() {
        Long id = userService.createUser(new User(null, "test_name", "test_email"))
                .getId();

        User user = userRepository.findById(id).orElseThrow();

        assertThat(user.getId(), notNullValue());
        assertThat(user.getName(), equalTo("test_name"));
        assertThat(user.getEmail(), equalTo("test_email"));
    }
}
