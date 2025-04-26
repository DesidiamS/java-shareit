package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItems;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceTest {

    @Mock
    private ItemRequestRepository mockItemRequestRepository;

    @Mock
    private UserService mockUserService;

    @Mock
    private ItemRepository mockItemRepository;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    @Test
    public void whenCreateItemRequestThenReturnItemRequest() {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("test description");
        Mockito
                .when(mockUserService.findById(Mockito.anyLong()))
                .thenReturn(Mockito.mock(User.class));
        ItemRequest expectedItemRequest = new ItemRequest(null, "test description", new User(),
                Timestamp.valueOf(LocalDateTime.now()));

        Mockito
                .when(mockItemRequestRepository.save(Mockito.any(ItemRequest.class)))
                .thenReturn(expectedItemRequest);

        ItemRequest itemRequest = itemRequestService.create(1L, itemRequestDto);

        assertThat(itemRequest, notNullValue());
        assertThat(itemRequest.getDescription(), equalTo("test description"));
    }

    @Test
    public void whenNotFoundByIdThenThrowException() {
        Mockito
                .when(mockItemRequestRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemRequestService.getById(-1L));
    }

    @Test
    public void whenGetRequestsWithResponseThenReturnMappedRequests() {
        Long userId = 1L;
        User user = new User(1L, "John", "john@email.ry");
        ItemRequest request1 = new ItemRequest(1L, "test", user, Timestamp.valueOf(LocalDateTime.now()));
        ItemRequest request2 = new ItemRequest(2L, "test2", user, Timestamp.valueOf(LocalDateTime.now()));
        Item item1 = new Item("test", "test", true, user, request1);

        Mockito.when(mockUserService.findById(userId)).thenReturn(user);
        Mockito.when(mockItemRequestRepository.findAllByRequestor(user))
                .thenReturn(List.of(request1, request2));
        Mockito.when(mockItemRepository.findAllByRequestIn(List.of(request1, request2)))
                .thenReturn(List.of(item1));

        Collection<ItemRequestWithItems> result = itemRequestService.getRequestsWithResponse(userId);

        assertThat(result, notNullValue());
        assertThat(2, equalTo(result.size()));
    }

    @Test
    public void whenGetAllThenReturnMappedRequests() {
        User user = new User(1L, "John", "john@email.ry");
        ItemRequest request1 = new ItemRequest(1L, "test", user, Timestamp.valueOf(LocalDateTime.now()));
        ItemRequest request2 = new ItemRequest(2L, "test2", user, Timestamp.valueOf(LocalDateTime.now()));
        Item item1 = new Item("test", "test", true, user, request1);

        Mockito.when(mockItemRequestRepository.findAll())
                .thenReturn(List.of(request1, request2));
        Mockito.when(mockItemRepository.findAllByRequestIn(List.of(request1, request2)))
                .thenReturn(List.of(item1));

        Collection<ItemRequestWithItems> result = itemRequestService.getAll();

        assertThat(result, notNullValue());
        assertThat(2, equalTo(result.size()));
    }

    @Test
    public void whenGetByIdThenReturnMappedRequest() {
        Long requestId = 1L;
        User user = new User(1L, "John", "john@email.ry");
        ItemRequest request = new ItemRequest(1L);
        request.setDescription("description");
        request.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        request.setRequestor(user);
        Item item = new Item("test", "test", true, user, request);

        Mockito.when(mockItemRequestRepository.findById(requestId)).thenReturn(Optional.of(request));
        Mockito.when(mockItemRepository.findAllByRequestIn(Collections.singletonList(request)))
                .thenReturn(List.of(item));

        ItemRequestWithItems result = itemRequestService.getById(requestId);

        assertThat(result, notNullValue());
        assertThat(requestId, equalTo(result.getId()));
        assertThat("description", equalTo(result.getDescription()));
        assertFalse(result.getItems().isEmpty());
        assertThat(1, equalTo(result.getItems().size()));
    }
}
