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
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserClient;
import ru.practicum.shareit.user.UserController;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserClient userClient;

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
    public void whenGetUserThenReturnOk() throws Exception {
        Mockito.when(userClient.getUser(anyLong()))
                .thenReturn(ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Collections.emptyMap()));

        mvc.perform(get("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenUserNotFoundThenReturnNotFound() throws Exception {
        Mockito.when(userClient.getUser(anyLong()))
                .thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        mvc.perform(get("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenCreateUserThenReturnOk() throws Exception {
        User user = new User();
        user.setId(null);
        user.setName("test");
        user.setEmail("test@mail.ru");
        String jsonRequestBody = objectMapper.writeValueAsString(user);

        Mockito.when(userClient.createUser(any()))
                .thenReturn(ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Collections.emptyMap()));

        mvc.perform(post("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenCreateUserWithWrongBodyThenReturnBadRequest() throws Exception {
        User user = new User();
        user.setId(null);
        user.setName("test");
        user.setEmail("test");
        String jsonRequestBody = objectMapper.writeValueAsString(user);

        mvc.perform(post("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenUpdateUserThenReturnOk() throws Exception {
        User user = new User();
        user.setId(null);
        user.setName("test");
        user.setEmail("test@mail.ru");
        String jsonRequestBody = objectMapper.writeValueAsString(user);

        Mockito.when(userClient.updateUser(anyLong(), any()))
                .thenReturn(ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Collections.emptyMap()));

        mvc.perform(patch("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenInvalidRequestBodyForUpdateUserThenReturnBadRequest() throws Exception {
        User user = new User();
        user.setId(null);
        user.setName("test");
        user.setEmail("test");
        String jsonRequestBody = objectMapper.writeValueAsString(user);

        mvc.perform(patch("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenDeleteUserThenReturnNoContent() throws Exception {
        Mockito.doNothing().when(userClient).deleteUser(anyLong());

        mvc.perform(delete("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
