package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithComment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.Constants.USER_HEADER;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {

    @Mock
    private ItemServiceImpl mockItemService;

    @Mock
    private UserServiceImpl mockUserService;

    private MockMvc mvc;

    @InjectMocks
    private ItemController itemController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mvc = MockMvcBuilders.standaloneSetup(itemController).build();
    }

    @Test
    public void whenGetAllItemsThenReturnOk() throws Exception {
        Mockito.when(mockItemService.getAllItemsByUserId(any()))
                .thenReturn(Collections.emptyList());

        mvc.perform(get("/items")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenGetItemByIdThenReturnOk() throws Exception {
        ItemWithComment mockItem = new ItemWithComment();
        Mockito.when(mockItemService.getItemById(anyLong()))
                .thenReturn(mockItem);

        mvc.perform(get("/items/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenCreateItemThenReturnOk() throws Exception {
        ItemDto itemDto = new ItemDto("test", "description", true, null);
        String jsonRequestBody = objectMapper.writeValueAsString(itemDto);

        Mockito.when(mockItemService.createItem(any(), any()))
                .thenReturn(new Item());

        Mockito
                .when(mockUserService.findById(any()))
                        .thenReturn(new User());

        mvc.perform(post("/items")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, 1L)
                        .content(jsonRequestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenUpdateItemThenReturnOk() throws Exception {
        ItemDto itemDto = new ItemDto("test", "description", false, null);
        String jsonRequestBody = objectMapper.writeValueAsString(itemDto);

        Mockito.when(mockItemService.updateItem(any(), anyLong(), any(ItemDto.class)))
                .thenReturn(new Item());

        mvc.perform(patch("/items/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, 1L)
                        .content(jsonRequestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenSearchItemThenReturnOk() throws Exception {
        Mockito.when(mockItemService.searchItem(anyString()))
                .thenReturn(Collections.emptyList());

        mvc.perform(get("/items/search")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("text", "test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenCreateCommentThenReturnOk() throws Exception {
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setText("test");
        String jsonRequestBody = objectMapper.writeValueAsString(commentRequest);

        Mockito.when(mockItemService.makeComment(any(CommentRequest.class), anyLong(), anyLong()))
                .thenReturn(new CommentDto(1L, "test", "author", Timestamp.valueOf(LocalDateTime.now())));

        mvc.perform(post("/items/1/comment")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, 1L)
                        .content(jsonRequestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
