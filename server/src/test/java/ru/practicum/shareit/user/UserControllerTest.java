package ru.practicum.shareit.user;

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
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void whenGetUserByIdThenReturnOk() throws Exception {
        Long userId = 1L;
        User mockUser = new User(userId, "John", "john@mail.ru");

        Mockito.when(userService.findById(userId)).thenReturn(mockUser);

        mvc.perform(get("/users/{id}", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(userId.intValue())))
                .andExpect(jsonPath("$.name", equalTo("John")))
                .andExpect(jsonPath("$.email", equalTo("john@mail.ru")));
    }

    @Test
    void whenCreateUserThenReturnUser() throws Exception {
        User user = new User(null, "John", "john@email.ru");
        String jsonRequestBody = objectMapper.writeValueAsString(user);

        User createdUser = new User(1L, "John", "john@email.ru");
        Mockito.when(userService.createUser(any(User.class))).thenReturn(createdUser);

        mvc.perform(post("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.name", equalTo("John")))
                .andExpect(jsonPath("$.email", equalTo("john@email.ru")));
    }

    @Test
    void whenUpdateUserThenReturnOk() throws Exception {
        Long userId = 1L;
        User updatedUser = new User(userId, "Ivan", "ivan@mail.ru");
        String jsonRequestBody = objectMapper.writeValueAsString(updatedUser);

        Mockito
                .when(userService.updateUser(any(), any()))
                .thenReturn(updatedUser);

        mvc.perform(patch("/users/{id}", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(userId.intValue())))
                .andExpect(jsonPath("$.name", equalTo("Ivan")))
                .andExpect(jsonPath("$.email", equalTo("ivan@mail.ru")));
    }

    @Test
    void whenDeleteUserThenOk() throws Exception {
        Long userId = 1L;

        Mockito.doNothing().when(userService).deleteUser(userId);

        mvc.perform(delete("/users/{id}", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
