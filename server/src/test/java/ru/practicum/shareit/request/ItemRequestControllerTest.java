package ru.practicum.shareit.request;

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
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItems;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.Constants.USER_HEADER;

@ExtendWith(MockitoExtension.class)
public class ItemRequestControllerTest {

    @Mock
    private ItemRequestServiceImpl itemRequestService;

    @InjectMocks
    private ItemRequestController itemRequestController;

    private MockMvc mvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mvc = MockMvcBuilders.standaloneSetup(itemRequestController).build();
    }

    @Test
    void whenCreateItemRequestThenReturnCreated() throws Exception {
        long userId = 1L;
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("test description");
        String jsonRequestBody = objectMapper.writeValueAsString(itemRequestDto);

        ItemRequest createdRequest = new ItemRequest(1L, "test description", new User(), Timestamp.valueOf(LocalDateTime.now()));
        Mockito.when(itemRequestService.create(any(), any(ItemRequestDto.class))).thenReturn(createdRequest);

        mvc.perform(post("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, Long.toString(userId))
                        .content(jsonRequestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.description", equalTo("test description")));
    }

    @Test
    void whenGetItemRequestsByUserThenReturnOk() throws Exception {
        Long userId = 1L;
        Collection<ItemRequestWithItems> mockRequests = List.of(
                new ItemRequestWithItems(1L, "test description", null, null, null)
        );

        Mockito.when(itemRequestService.getRequestsWithResponse(userId)).thenReturn(mockRequests);

        mvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", equalTo(1)))
                .andExpect(jsonPath("$[0].description", equalTo("test description")));
    }

    @Test
    void whenGetAllItemRequestsThenReturnOk() throws Exception {
        Collection<ItemRequestWithItems> mockRequests = List.of(
                new ItemRequestWithItems(1L, "test description", null, null, null),
                new ItemRequestWithItems(2L, "test description 2", null, null, null)
        );

        Mockito.when(itemRequestService.getAll()).thenReturn(mockRequests);

        mvc.perform(get("/requests/all")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", equalTo(1)))
                .andExpect(jsonPath("$[0].description", equalTo("test description")))
                .andExpect(jsonPath("$[1].id", equalTo(2)))
                .andExpect(jsonPath("$[1].description", equalTo("test description 2")));
    }

    @Test
    void whenGetItemRequestByIdThenReturnOk() throws Exception {
        Long requestId = 1L;
        ItemRequestWithItems mockRequest = new ItemRequestWithItems(requestId, "test description", null, null, null);

        Mockito.when(itemRequestService.getById(requestId)).thenReturn(mockRequest);

        mvc.perform(get("/requests/{id}", requestId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(requestId.intValue())))
                .andExpect(jsonPath("$.description", equalTo("test description")));
    }
}
